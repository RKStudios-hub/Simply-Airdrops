package com.rks.airdrop.recipe;

import com.mojang.serialization.MapCodec;
import com.rks.airdrop.config.AirdropSettings;
import com.rks.airdrop.registry.ModConditions;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class RadioControllerCraftableCondition implements ICondition {
    public static final MapCodec<RadioControllerCraftableCondition> CODEC = MapCodec.unit(RadioControllerCraftableCondition::new);

    @Override
    public boolean test(IContext context) {
        AirdropSettings.load();
        return AirdropSettings.radioControllerCraftable();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return ModConditions.RADIO_CONTROLLER_CRAFTABLE.get();
    }
}
