package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.WeaponCrateBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WeaponCrateModel extends GeoModel<WeaponCrateBlockEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/weapon_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/weapon_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/weapon_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(WeaponCrateBlockEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(WeaponCrateBlockEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(WeaponCrateBlockEntity animatable) {
        return ANIMATION;
    }
}
