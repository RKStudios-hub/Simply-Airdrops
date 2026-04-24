package com.rks.airdrop.block;

import com.rks.airdrop.blockentity.AmmoCrateBlockEntity;
import com.rks.airdrop.registry.ModBlockEntities;
import com.rks.airdrop.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AmmoCrateBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AmmoCrateBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection().getOpposite();

        for (int[] offset : AmmoCrateShapeCache.getOccupiedOffsets(facing)) {
            BlockPos helperPos = clickedPos.offset(offset[0], 0, offset[2]);
            if (!level.getBlockState(helperPos).canBeReplaced(context)) {
                return null;
            }
        }

        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AmmoCrateBlockEntity(pos, state);
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
        if (blockEntity instanceof AmmoCrateBlockEntity ammoCrate && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, ammoCrate, pos);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide) {
            return;
        }

        for (int[] offset : AmmoCrateShapeCache.getOccupiedOffsets(state.getValue(FACING))) {
            BlockPos helperPos = pos.offset(offset[0], 0, offset[2]);
            level.setBlock(helperPos, ModBlocks.AMMO_CRATE_HELPER.get().defaultBlockState()
                    .setValue(AmmoCrateHelperBlock.OFFSET_X, AmmoCrateHelperBlock.encodeOffset(offset[0]))
                    .setValue(AmmoCrateHelperBlock.OFFSET_Z, AmmoCrateHelperBlock.encodeOffset(offset[2])), 3);
        }

        if (stack.hasCustomHoverName() && level.getBlockEntity(pos) instanceof AmmoCrateBlockEntity ammoCrate) {
            ammoCrate.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                removeHelpers(level, pos);
            }

            if (level.getBlockEntity(pos) instanceof AmmoCrateBlockEntity) {
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
        return blockEntity instanceof AmmoCrateBlockEntity ammoCrate
                ? AbstractContainerMenu.getRedstoneSignalFromContainer(ammoCrate)
                : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AmmoCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AmmoCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return AmmoCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof AmmoCrateBlockEntity ammoCrate)) {
            return super.getDrops(state, builder);
        }

        ItemStack stack = new ItemStack(asItem());
        CompoundTag blockEntityTag = ammoCrate.saveWithoutMetadata();
        blockEntityTag.remove("id");
        blockEntityTag.remove("x");
        blockEntityTag.remove("y");
        blockEntityTag.remove("z");

        if (!blockEntityTag.isEmpty()) {
            stack.addTagElement("BlockEntityTag", blockEntityTag);
        }

        if (ammoCrate.hasCustomName()) {
            stack.setHoverName(ammoCrate.getCustomName());
        }

        return List.of(stack);
    }

    private static void removeHelpers(Level level, BlockPos origin) {
        BlockState state = level.getBlockState(origin);
        Direction facing = state.getBlock() instanceof AmmoCrateBlock ? state.getValue(FACING) : Direction.NORTH;
        for (int[] offset : AmmoCrateShapeCache.getOccupiedOffsets(facing)) {
            BlockPos helperPos = origin.offset(offset[0], 0, offset[2]);
            if (level.getBlockState(helperPos).getBlock() instanceof AmmoCrateHelperBlock) {
                level.removeBlock(helperPos, false);
            }
        }
    }
}
