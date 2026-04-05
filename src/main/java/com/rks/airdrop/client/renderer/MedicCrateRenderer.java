package com.rks.airdrop.client.renderer;

import com.rks.airdrop.blockentity.MedicCrateBlockEntity;
import com.rks.airdrop.client.model.MedicCrateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MedicCrateRenderer extends GeoBlockRenderer<MedicCrateBlockEntity> {
    public MedicCrateRenderer(BlockEntityRendererProvider.Context context) {
        super(new MedicCrateModel());
    }
}
