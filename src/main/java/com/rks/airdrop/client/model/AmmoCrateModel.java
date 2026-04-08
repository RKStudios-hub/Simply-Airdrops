package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.AmmoCrateBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AmmoCrateModel extends GeoModel<AmmoCrateBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/ammo_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/ammo_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/ammo_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(AmmoCrateBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AmmoCrateBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AmmoCrateBlockEntity animatable) {
        return ANIMATION;
    }
}
