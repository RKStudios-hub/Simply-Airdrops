package com.rks.airdrop.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rks.airdrop.client.model.MedicCrateItemModel;
import com.rks.airdrop.item.MedicCrateItem;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MedicCrateItemRenderer extends GeoItemRenderer<MedicCrateItem> {
    public MedicCrateItemRenderer() {
        super(new MedicCrateItemModel());
        withScale(0.65F);
    }

    @Override
    public void preRender(PoseStack poseStack, MedicCrateItem animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
    }
}
