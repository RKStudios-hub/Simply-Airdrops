package com.rks.airdrop.item;

import com.rks.airdrop.entity.AirdropEntity;
import com.rks.airdrop.registry.ModEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadioControllerItem extends Item {
    private static final int HORIZONTAL_RADIUS = 32;
    private static final double SPAWN_Y = 400.0D;

    public RadioControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            AirdropEntity airdrop = ModEntities.AIRDROP.get().create(level);
            if (airdrop == null) {
                return InteractionResultHolder.fail(stack);
            }

            int spawnX = Mth.floor(player.getX()) + level.random.nextInt(HORIZONTAL_RADIUS * 2 + 1) - HORIZONTAL_RADIUS;
            int spawnZ = Mth.floor(player.getZ()) + level.random.nextInt(HORIZONTAL_RADIUS * 2 + 1) - HORIZONTAL_RADIUS;

            airdrop.moveTo(spawnX + 0.5D, SPAWN_Y, spawnZ + 0.5D, level.random.nextFloat() * 360.0F, 0.0F);
            airdrop.setDescending(true);
            level.addFreshEntity(airdrop);

            player.displayClientMessage(
                    Component.literal("[Radio Controller] ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal("Airdrop inbound ").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("X: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(spawnX)).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Z: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(spawnZ)).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Y: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString((int) SPAWN_Y)).withStyle(ChatFormatting.AQUA)),
                    false
            );
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
