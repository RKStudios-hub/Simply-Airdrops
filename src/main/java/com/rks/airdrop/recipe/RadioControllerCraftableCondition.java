package com.rks.airdrop.recipe;

import com.google.gson.JsonObject;
import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.config.AirdropSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public final class RadioControllerCraftableCondition implements ICondition {
    private static final ResourceLocation ID = new ResourceLocation(RksAirdrops.MODID, "radio_controller_craftable");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        AirdropSettings.load();
        return AirdropSettings.radioControllerCraftable();
    }

    public static final class Serializer implements IConditionSerializer<RadioControllerCraftableCondition> {
        @Override
        public void write(JsonObject json, RadioControllerCraftableCondition value) {
        }

        @Override
        public RadioControllerCraftableCondition read(JsonObject json) {
            return new RadioControllerCraftableCondition();
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
