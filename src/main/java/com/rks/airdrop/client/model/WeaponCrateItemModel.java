package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.WeaponCrateItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WeaponCrateItemModel extends GeoModel<WeaponCrateItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/weapon_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/weapon_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/weapon_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(WeaponCrateItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(WeaponCrateItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(WeaponCrateItem animatable) {
        return ANIMATION;
    }
}
