package com.rks.airdrop.client.model;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.AmmoCrateItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AmmoCrateItemModel extends GeoModel<AmmoCrateItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(RksAirdrops.MODID, "geo/ammo_crate.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(RksAirdrops.MODID, "textures/block/ammo_crate.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(RksAirdrops.MODID, "animations/ammo_crate.animation.json");

    @Override
    public ResourceLocation getModelResource(AmmoCrateItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AmmoCrateItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AmmoCrateItem animatable) {
        return ANIMATION;
    }
}
