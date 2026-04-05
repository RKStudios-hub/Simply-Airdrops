package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.MedicCrateBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MedicCrateModel extends GeoModel<MedicCrateBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/medic_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/medic_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/medic_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(MedicCrateBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MedicCrateBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MedicCrateBlockEntity animatable) {
        return ANIMATION;
    }
}
