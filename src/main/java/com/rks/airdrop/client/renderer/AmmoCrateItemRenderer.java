package com.rks.airdrop.client.renderer;

import com.rks.airdrop.client.model.AmmoCrateItemModel;
import com.rks.airdrop.item.AmmoCrateItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AmmoCrateItemRenderer extends GeoItemRenderer<AmmoCrateItem> {
    public AmmoCrateItemRenderer() {
        super(new AmmoCrateItemModel());
        withScale(0.45F);
    }
}
