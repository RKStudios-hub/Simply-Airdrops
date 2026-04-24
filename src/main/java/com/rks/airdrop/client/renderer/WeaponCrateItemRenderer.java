package com.rks.airdrop.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rks.airdrop.client.model.WeaponCrateItemModel;
import com.rks.airdrop.item.WeaponCrateItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WeaponCrateItemRenderer extends GeoItemRenderer<WeaponCrateItem> {
    public WeaponCrateItemRenderer() {
        super(new WeaponCrateItemModel());
        withScale(0.45F);
    }

    @Override
    public void preRender(PoseStack poseStack, WeaponCrateItem animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        if (this.renderPerspective == ItemDisplayContext.GUI) {
            poseStack.translate(-0.10D, 0.0D, 0.0D);
            poseStack.scale(1.24F, 1.24F, 1.24F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
    }
}
