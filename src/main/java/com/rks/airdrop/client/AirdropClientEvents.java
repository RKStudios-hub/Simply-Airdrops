package com.rks.airdrop.client;

import com.rks.airdrop.client.renderer.AirdropBoxRenderer;
import com.rks.airdrop.client.renderer.AirdropEntityRenderer;
import com.rks.airdrop.client.renderer.AmmoCrateRenderer;
import com.rks.airdrop.client.renderer.MedicCrateRenderer;
import com.rks.airdrop.client.renderer.WeaponCrateRenderer;
import com.rks.airdrop.registry.ModBlockEntities;
import com.rks.airdrop.registry.ModEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class AirdropClientEvents {
    private AirdropClientEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.AIRDROP.get(), AirdropEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AIRDROP_BOX.get(), AirdropBoxRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AMMO_CRATE.get(), AmmoCrateRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MEDIC_CRATE.get(), MedicCrateRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WEAPON_CRATE.get(), WeaponCrateRenderer::new);
    }
}



