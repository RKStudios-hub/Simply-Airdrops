package com.rks.airdrop.client.renderer;

import com.rks.airdrop.blockentity.AmmoCrateBlockEntity;
import com.rks.airdrop.client.model.AmmoCrateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AmmoCrateRenderer extends GeoBlockRenderer<AmmoCrateBlockEntity> {
    public AmmoCrateRenderer(BlockEntityRendererProvider.Context context) {
        super(new AmmoCrateModel());
    }
}
