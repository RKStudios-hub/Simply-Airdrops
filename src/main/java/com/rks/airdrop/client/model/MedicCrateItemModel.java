package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.MedicCrateItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MedicCrateItemModel extends GeoModel<MedicCrateItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/medic_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/medic_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/medic_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(MedicCrateItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MedicCrateItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MedicCrateItem animatable) {
        return ANIMATION;
    }
}
