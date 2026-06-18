package com.rks.airdrop.item;

import com.rks.airdrop.client.renderer.RadioControllerItemRenderer;
import com.rks.airdrop.config.AirdropSettings;
import com.rks.airdrop.registry.ModSounds;
import com.rks.airdrop.world.AirdropSpawner;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;


public class RadioControllerItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RadioControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            AirdropSpawner.SpawnResult result = AirdropSpawner.spawnNearPlayer(serverLevel, serverPlayer);
            if (result == null) {
                return InteractionResultHolder.fail(stack);
            }

            if (AirdropSettings.radioControllerConsumed() && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            player.displayClientMessage(
                    Component.literal("[Radio Controller] ").withStyle(ChatFormatting.GOLD)
                            .append(Component.literal("Airdrop inbound ").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("X: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getX())).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Z: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getZ())).withStyle(ChatFormatting.RED))
                            .append(Component.literal(" Y: ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal(Integer.toString(result.spawnPos().getY())).withStyle(ChatFormatting.AQUA)),
                    false
            );

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.randomAirplaneSound(level.random), SoundSource.PLAYERS, 2.0F, 1.0F);
            player.playNotifySound(SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.PLAYERS, 0.8F, 1.6F);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private RadioControllerItemRenderer renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (renderer == null) {
                    renderer = new RadioControllerItemRenderer();
                }

                return renderer;
            }
        });
    }
}




