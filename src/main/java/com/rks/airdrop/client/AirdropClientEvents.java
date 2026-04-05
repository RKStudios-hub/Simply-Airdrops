package com.rks.airdrop.client;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.client.renderer.AirdropBoxRenderer;
import com.rks.airdrop.client.renderer.MedicCrateRenderer;
import com.rks.airdrop.registry.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RksAirdrops.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class AirdropClientEvents {
    private AirdropClientEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.AIRDROP_BOX.get(), AirdropBoxRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MEDIC_CRATE.get(), MedicCrateRenderer::new);
    }
}
