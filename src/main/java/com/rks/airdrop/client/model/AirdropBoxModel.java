package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.AirdropBoxBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AirdropBoxModel extends GeoModel<AirdropBoxBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/airdrop_box.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/airdrop_box.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/airdrop_box.animation.json");

    @Override
    public ResourceLocation getModelResource(AirdropBoxBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AirdropBoxBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AirdropBoxBlockEntity animatable) {
        return ANIMATION;
    }
}
