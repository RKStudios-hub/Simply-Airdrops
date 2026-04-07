package com.rks.airdrop.client.renderer;

import com.rks.airdrop.client.model.AirdropEntityModel;
import com.rks.airdrop.entity.AirdropEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AirdropEntityRenderer extends GeoEntityRenderer<AirdropEntity> {
    public AirdropEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AirdropEntityModel());
        this.shadowRadius = 0.8F;
    }
}
