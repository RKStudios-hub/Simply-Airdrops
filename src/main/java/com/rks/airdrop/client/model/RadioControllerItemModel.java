package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.RadioControllerItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RadioControllerItemModel extends GeoModel<RadioControllerItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/radio_controller.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/item/radio_controller.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/radio_controller.animation.json");

    @Override
    public ResourceLocation getModelResource(RadioControllerItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(RadioControllerItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(RadioControllerItem animatable) {
        return ANIMATION;
    }
}
