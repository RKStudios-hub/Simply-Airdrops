package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.entity.AirdropEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AirdropEntityModel extends GeoModel<AirdropEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, "geo/airdrop.geo.json");
    private static final ResourceLocation DESCENDING_MODEL =
            ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, "geo/airdrop_with_parachute.geo.json");
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, "textures/entity/airdrop.png");
    private static final ResourceLocation DESCENDING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, "textures/entity/airdrop_with_parachute.png");
    private static final ResourceLocation ANIMATION =
            ResourceLocation.fromNamespaceAndPath(RksAirdrops.MODID, "animations/airdrop.animation.json");

    @Override
    public ResourceLocation getModelResource(AirdropEntity animatable) {
        return animatable.isDescending() ? DESCENDING_MODEL : MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AirdropEntity animatable) {
        return animatable.isDescending() ? DESCENDING_TEXTURE : TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AirdropEntity animatable) {
        return ANIMATION;
    }
}



