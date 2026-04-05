package com.rks.airdrop.client.renderer;

import com.rks.airdrop.blockentity.AirdropBoxBlockEntity;
import com.rks.airdrop.client.model.AirdropBoxModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AirdropBoxRenderer extends GeoBlockRenderer<AirdropBoxBlockEntity> {
    public AirdropBoxRenderer(BlockEntityRendererProvider.Context context) {
        super(new AirdropBoxModel());
    }
}
