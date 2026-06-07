package com.rks.airdrop.registry;

import com.mojang.serialization.Codec;
import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.loot.FlareGunLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RksAirdrops.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> FLARE_GUN_CHEST_LOOT =
            LOOT_MODIFIERS.register("flare_gun_chest_loot", () -> FlareGunLootModifier.CODEC);

    private ModLootModifiers() {
    }
}
