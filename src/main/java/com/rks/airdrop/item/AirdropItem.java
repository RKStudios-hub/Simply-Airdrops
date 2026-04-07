package com.rks.airdrop.item;

import com.rks.airdrop.entity.AirdropEntity;
import com.rks.airdrop.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AirdropItem extends Item {
    public AirdropItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (!(hitResult instanceof BlockHitResult blockHitResult)) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos spawnPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
        Vec3 center = Vec3.atBottomCenterOf(spawnPos);

        AirdropEntity airdrop = ModEntities.AIRDROP.get().create(level);
        if (airdrop == null) {
            return InteractionResultHolder.fail(stack);
        }

        airdrop.moveTo(center.x, center.y, center.z, player.getYRot(), 0.0F);

        if (!level.noCollision(airdrop, airdrop.getBoundingBox())) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            level.addFreshEntity(airdrop);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
