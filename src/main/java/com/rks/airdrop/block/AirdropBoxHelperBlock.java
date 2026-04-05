package com.rks.airdrop.block;

import com.rks.airdrop.blockentity.AirdropBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AirdropBoxHelperBlock extends Block {
    public static final IntegerProperty OFFSET_X = IntegerProperty.create("offset_x", 0, 2);
    public static final IntegerProperty OFFSET_Y = IntegerProperty.create("offset_y", 0, 1);
    public static final IntegerProperty OFFSET_Z = IntegerProperty.create("offset_z", 0, 2);

    public AirdropBoxHelperBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(OFFSET_X, 0)
                .setValue(OFFSET_Y, 0)
                .setValue(OFFSET_Z, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AirdropBoxShapeCache.getDetailedShape(getFacing(level, state, pos),
                decodeOffset(state.getValue(OFFSET_X)), state.getValue(OFFSET_Y), decodeOffset(state.getValue(OFFSET_Z)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AirdropBoxShapeCache.getSolidShape(getFacing(level, state, pos),
                decodeOffset(state.getValue(OFFSET_X)), state.getValue(OFFSET_Y), decodeOffset(state.getValue(OFFSET_Z)));
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return AirdropBoxShapeCache.getDetailedShape(getFacing(level, state, pos),
                decodeOffset(state.getValue(OFFSET_X)), state.getValue(OFFSET_Y), decodeOffset(state.getValue(OFFSET_Z)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos masterPos = getMasterPos(state, pos);
        BlockState masterState = level.getBlockState(masterPos);
        if (!(masterState.getBlock() instanceof AirdropBoxBlock)) {
            if (!level.isClientSide) {
                level.removeBlock(pos, false);
            }
            return InteractionResult.PASS;
        }

        return AirdropBoxBlock.openContainer(level, masterPos, player);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        BlockPos masterPos = getMasterPos(state, pos);
        BlockState masterState = level.getBlockState(masterPos);
        if (masterState.getBlock() instanceof AirdropBoxBlock) {
            return Math.min(1.0F, masterState.getDestroyProgress(player, level, masterPos) * 1.5F);
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos masterPos = getMasterPos(state, pos);
        if (!masterPos.equals(pos) && level.getBlockState(masterPos).getBlock() instanceof AirdropBoxBlock) {
            level.destroyBlock(masterPos, !player.isCreative(), player);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return null;
    }

    private static BlockPos getMasterPos(BlockState state, BlockPos pos) {
        return pos.offset(-decodeOffset(state.getValue(OFFSET_X)), -state.getValue(OFFSET_Y), -decodeOffset(state.getValue(OFFSET_Z)));
    }

    private static Direction getFacing(BlockGetter level, BlockState state, BlockPos pos) {
        BlockState masterState = level.getBlockState(getMasterPos(state, pos));
        if (masterState.getBlock() instanceof AirdropBoxBlock) {
            return masterState.getValue(AirdropBoxBlock.FACING);
        }

        return Direction.NORTH;
    }

    public static int encodeOffset(int offset) {
        return offset + 1;
    }

    private static int decodeOffset(int encodedOffset) {
        return encodedOffset - 1;
    }
}
