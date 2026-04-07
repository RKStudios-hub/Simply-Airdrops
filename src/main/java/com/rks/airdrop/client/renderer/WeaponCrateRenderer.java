package com.rks.airdrop.client.renderer;

import com.rks.airdrop.blockentity.WeaponCrateBlockEntity;
import com.rks.airdrop.client.model.WeaponCrateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class WeaponCrateRenderer extends GeoBlockRenderer<WeaponCrateBlockEntity> {
    public WeaponCrateRenderer(BlockEntityRendererProvider.Context context) {
        super(new WeaponCrateModel());
    }
}
