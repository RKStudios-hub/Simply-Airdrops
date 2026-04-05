package com.rks.airdrop.block;

import com.rks.airdrop.blockentity.MedicCrateBlockEntity;
import com.rks.airdrop.registry.ModBlockEntities;
import com.rks.airdrop.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MedicCrateBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final int[][] HELPER_OFFSETS = createHelperOffsets();

    public MedicCrateBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(OPEN, Boolean.FALSE));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();

        for (int[] offset : HELPER_OFFSETS) {
            BlockPos helperPos = clickedPos.offset(offset[0], 0, offset[2]);
            if (!level.getBlockState(helperPos).canBeReplaced(context)) {
                return null;
            }
        }

        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(OPEN, Boolean.FALSE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MedicCrateBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return openContainer(level, pos, player);
    }

    public static InteractionResult openContainer(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MedicCrateBlockEntity medicCrate && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, medicCrate, pos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide) {
            return;
        }

        for (int[] offset : HELPER_OFFSETS) {
            BlockPos helperPos = pos.offset(offset[0], 0, offset[2]);
            level.setBlock(helperPos, ModBlocks.MEDIC_CRATE_HELPER.get().defaultBlockState()
                    .setValue(MedicCrateHelperBlock.OFFSET_X, MedicCrateHelperBlock.encodeOffset(offset[0]))
                    .setValue(MedicCrateHelperBlock.OFFSET_Z, MedicCrateHelperBlock.encodeOffset(offset[2])), 3);
        }

        if (stack.hasCustomHoverName() && level.getBlockEntity(pos) instanceof MedicCrateBlockEntity medicCrate) {
            medicCrate.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                removeHelpers(level, pos);
            }

            if (level.getBlockEntity(pos) instanceof MedicCrateBlockEntity) {
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MedicCrateBlockEntity medicCrate
                ? AbstractContainerMenu.getRedstoneSignalFromContainer(medicCrate)
                : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide
                ? null
                : createTickerHelper(blockEntityType, ModBlockEntities.MEDIC_CRATE.get(), MedicCrateBlockEntity::recheckOpen);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return MedicCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return MedicCrateShapeCache.getSolidShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return MedicCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof MedicCrateBlockEntity medicCrate)) {
            return super.getDrops(state, builder);
        }

        ItemStack stack = new ItemStack(asItem());
        CompoundTag blockEntityTag = medicCrate.saveWithoutMetadata();
        blockEntityTag.remove("id");
        blockEntityTag.remove("x");
        blockEntityTag.remove("y");
        blockEntityTag.remove("z");

        if (!blockEntityTag.isEmpty()) {
            stack.addTagElement("BlockEntityTag", blockEntityTag);
        }

        if (medicCrate.hasCustomName()) {
            stack.setHoverName(medicCrate.getCustomName());
        }

        return List.of(stack);
    }

    private static void removeHelpers(Level level, BlockPos origin) {
        for (int[] offset : HELPER_OFFSETS) {
            BlockPos helperPos = origin.offset(offset[0], 0, offset[2]);
            if (level.getBlockState(helperPos).getBlock() instanceof MedicCrateHelperBlock) {
                level.removeBlock(helperPos, false);
            }
        }
    }

    private static int[][] createHelperOffsets() {
        int[][] offsets = new int[8][3];
        int index = 0;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                offsets[index++] = new int[]{x, 0, z};
            }
        }

        return offsets;
    }
}
