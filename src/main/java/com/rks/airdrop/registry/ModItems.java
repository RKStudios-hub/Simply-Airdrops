package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.AirdropBoxItem;
import com.rks.airdrop.item.AirdropItem;
import com.rks.airdrop.item.AmmoCrateItem;
import com.rks.airdrop.item.MedicCrateItem;
import com.rks.airdrop.item.RadioControllerItem;
import com.rks.airdrop.item.WeaponCrateItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RksAirdrops.MODID);

    public static final RegistryObject<Item> AIRDROP_BOX = ITEMS.register(
            "airdrop_box",
            () -> new AirdropBoxItem(ModBlocks.AIRDROP_BOX.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> AIRDROP = ITEMS.register(
            "airdrop",
            () -> new AirdropItem(new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> MEDIC_CRATE = ITEMS.register(
            "medic_crate",
            () -> new MedicCrateItem(ModBlocks.MEDIC_CRATE.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> AMMO_CRATE = ITEMS.register(
            "ammo_crate",
            () -> new AmmoCrateItem(ModBlocks.AMMO_CRATE.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> WEAPON_CRATE = ITEMS.register(
            "weapon_crate",
            () -> new WeaponCrateItem(ModBlocks.WEAPON_CRATE.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<Item> RADIO_CONTROLLER = ITEMS.register(
            "radio_controller",
            () -> new RadioControllerItem(new Item.Properties().stacksTo(1))
    );

    private ModItems() {
    }
}
