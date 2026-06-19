package com.rks.airdrop.registry;

import com.mojang.serialization.MapCodec;
import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.loot.FlareGunLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RksAirdrops.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<? extends IGlobalLootModifier>> FLARE_GUN_CHEST_LOOT =
            LOOT_MODIFIERS.register("flare_gun_chest_loot", () -> FlareGunLootModifier.CODEC);

    private ModLootModifiers() {
    }
}




