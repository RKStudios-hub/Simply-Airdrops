package com.rks.airdrop.client.renderer;

import com.rks.airdrop.client.model.AirdropBoxItemModel;
import com.rks.airdrop.item.AirdropBoxItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AirdropBoxItemRenderer extends GeoItemRenderer<AirdropBoxItem> {
    public AirdropBoxItemRenderer() {
        super(new AirdropBoxItemModel());
        withScale(0.45F);
    }

    @Override
    public void preRender(PoseStack poseStack, AirdropBoxItem animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        poseStack.translate(0.15D, 0.0D, 0.0D);
        if (this.renderPerspective == ItemDisplayContext.GUI) {
            poseStack.translate(0.08D, -0.10D, 0.0D);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
    }
}
