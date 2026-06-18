package com.rks.airdrop.registry;

import com.mojang.serialization.MapCodec;
import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.recipe.RadioControllerCraftableCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, RksAirdrops.MODID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<RadioControllerCraftableCondition>> RADIO_CONTROLLER_CRAFTABLE =
            CONDITIONS.register("radio_controller_craftable", () -> RadioControllerCraftableCondition.CODEC);

    private ModConditions() {
    }
}
