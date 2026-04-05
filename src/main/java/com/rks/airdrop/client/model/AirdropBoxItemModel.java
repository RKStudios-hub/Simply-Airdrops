package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.AirdropBoxItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AirdropBoxItemModel extends GeoModel<AirdropBoxItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/airdrop_box.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/airdrop_box.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/airdrop_box.animation.json");

    @Override
    public ResourceLocation getModelResource(AirdropBoxItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AirdropBoxItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AirdropBoxItem animatable) {
        return ANIMATION;
    }
}
