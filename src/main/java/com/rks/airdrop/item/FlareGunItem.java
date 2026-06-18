package com.rks.airdrop.item;

import com.rks.airdrop.registry.ModSounds;
import com.rks.airdrop.world.AirdropSpawner;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FlareGunItem extends Item {
    private static final DustParticleOptions FLARE_PARTICLE =
            new DustParticleOptions(new Vector3f(1.0F, 0.05F, 0.02F), 1.5F);
    private static final int COOLDOWN_TICKS = 20 * 20;

    public FlareGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            AirdropSpawner.SpawnResult result = AirdropSpawner.spawnNearPlayer(serverLevel, serverPlayer);
            if (result == null) {
                return InteractionResultHolder.fail(stack);
            }

            spawnFlareShot(serverLevel, player);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.2F, 0.85F);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.randomAirplaneSound(level.random), SoundSource.PLAYERS, 2.0F, 1.0F);

            player.displayClientMessage(
                    Component.literal("[Flare Gun] ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal("Airdrop inbound ").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("X: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getX())).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Z: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getZ())).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Y: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getY())).withStyle(ChatFormatting.AQUA)),
                    false
            );

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static void spawnFlareShot(ServerLevel level, Player player) {
        Vec3 start = player.getEyePosition().add(player.getLookAngle().scale(0.9D));
        Vec3 end = start.add(player.getLookAngle().scale(14.0D)).add(0.0D, 10.0D, 0.0D);
        Vec3 travel = end.subtract(start);

        for (int i = 0; i <= 36; i++) {
            double progress = i / 36.0D;
            Vec3 point = start.add(travel.scale(progress));
            level.sendParticles(FLARE_PARTICLE, point.x, point.y, point.z, 2, 0.04D, 0.04D, 0.04D, 0.0D);
            if (i % 3 == 0) {
                level.sendParticles(ParticleTypes.SMOKE, point.x, point.y, point.z, 1, 0.02D, 0.02D, 0.02D, 0.0D);
            }
        }

        level.sendParticles(ParticleTypes.FIREWORK, end.x, end.y, end.z, 40, 0.8D, 0.8D, 0.8D, 0.08D);
        level.sendParticles(FLARE_PARTICLE, end.x, end.y, end.z, 60, 1.2D, 1.2D, 1.2D, 0.04D);
    }
}




