package com.rks.airdrop.block;

import com.rks.airdrop.blockentity.WeaponCrateBlockEntity;
import com.rks.airdrop.registry.ModBlockEntities;
import com.rks.airdrop.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WeaponCrateBlock extends BaseEntityBlock {
    public static final MapCodec<WeaponCrateBlock> CODEC = simpleCodec(WeaponCrateBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public WeaponCrateBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, Boolean.FALSE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection().getOpposite();

        for (int[] offset : WeaponCrateShapeCache.getOccupiedOffsets(facing)) {
            BlockPos helperPos = clickedPos.offset(offset[0], 0, offset[2]);
            if (!level.getBlockState(helperPos).canBeReplaced(context)) {
                return null;
            }
        }

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(OPEN, Boolean.FALSE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WeaponCrateBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return openContainer(level, pos, player);
    }

    public static InteractionResult openContainer(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof WeaponCrateBlockEntity weaponCrate && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(weaponCrate, pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (level.isClientSide) {
            return;
        }

                BlockEntity blockEntity = level.getBlockEntity(pos);
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntity != null && blockEntityData != null) {
            blockEntityData.loadInto(blockEntity, level.registryAccess());
            blockEntity.setChanged();
        }

for (int[] offset : WeaponCrateShapeCache.getOccupiedOffsets(state.getValue(FACING))) {
            BlockPos helperPos = pos.offset(offset[0], 0, offset[2]);
            level.setBlock(helperPos, ModBlocks.WEAPON_CRATE_HELPER.get().defaultBlockState()
                    .setValue(WeaponCrateHelperBlock.OFFSET_X, WeaponCrateHelperBlock.encodeOffset(offset[0]))
                    .setValue(WeaponCrateHelperBlock.OFFSET_Z, WeaponCrateHelperBlock.encodeOffset(offset[2])), 3);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                removeHelpers(level, pos);
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WeaponCrateBlockEntity) {
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
        return blockEntity instanceof WeaponCrateBlockEntity weaponCrate
                ? AbstractContainerMenu.getRedstoneSignalFromContainer(weaponCrate)
                : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide
                ? null
                : createTickerHelper(blockEntityType, ModBlockEntities.WEAPON_CRATE.get(), WeaponCrateBlockEntity::recheckOpen);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return WeaponCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return WeaponCrateShapeCache.getSolidShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return WeaponCrateShapeCache.getDetailedShape(state.getValue(FACING), 0, 0, 0);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof WeaponCrateBlockEntity weaponCrate)) {
            return super.getDrops(state, builder);
        }

        ItemStack stack = new ItemStack(asItem());
        CompoundTag blockEntityTag = weaponCrate.saveWithoutMetadata(builder.getLevel().registryAccess());
        blockEntityTag.putString("id", "rks_airdrops:weapon_crate");
        blockEntityTag.remove("x");
        blockEntityTag.remove("y");
        blockEntityTag.remove("z");

        if (!blockEntityTag.isEmpty()) {
            stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));
        }

        if (weaponCrate.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, weaponCrate.getCustomName());
        }

        return List.of(stack);
    }

    private static void removeHelpers(Level level, BlockPos origin) {
        BlockState state = level.getBlockState(origin);
        Direction facing = state.getBlock() instanceof WeaponCrateBlock ? state.getValue(FACING) : Direction.NORTH;
        for (int[] offset : WeaponCrateShapeCache.getOccupiedOffsets(facing)) {
            BlockPos helperPos = origin.offset(offset[0], 0, offset[2]);
            if (level.getBlockState(helperPos).getBlock() instanceof WeaponCrateHelperBlock) {
                level.removeBlock(helperPos, false);
            }
        }
    }
}





