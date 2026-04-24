package com.rks.airdrop.client.renderer;

import com.rks.airdrop.client.model.AmmoCrateItemModel;
import com.rks.airdrop.item.AmmoCrateItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AmmoCrateItemRenderer extends GeoItemRenderer<AmmoCrateItem> {
    public AmmoCrateItemRenderer() {
        super(new AmmoCrateItemModel());
        withScale(0.55F);
    }

    @Override
    public void preRender(PoseStack poseStack, AmmoCrateItem animatable, BakedGeoModel model,
                          MultiBufferSource bufferSource, com.mojang.blaze3d.vertex.VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        if (this.renderPerspective == ItemDisplayContext.GUI) {
            poseStack.translate(0.0D, 0.60D, 0.0D);
            poseStack.scale(3.0F, 3.0F, 3.0F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
                red, green, blue, alpha);
    }
}
