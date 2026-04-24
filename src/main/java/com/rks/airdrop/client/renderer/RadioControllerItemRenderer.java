package com.rks.airdrop.client.renderer;

import com.rks.airdrop.client.model.RadioControllerItemModel;
import com.rks.airdrop.item.RadioControllerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RadioControllerItemRenderer extends GeoItemRenderer<RadioControllerItem> {
    public RadioControllerItemRenderer() {
        super(new RadioControllerItemModel());
    }
}
