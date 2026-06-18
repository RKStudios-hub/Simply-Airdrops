package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.item.AirdropBoxItem;
import com.rks.airdrop.item.AirdropItem;
import com.rks.airdrop.item.AmmoCrateItem;
import com.rks.airdrop.item.FlareGunItem;
import com.rks.airdrop.item.MedicCrateItem;
import com.rks.airdrop.item.RadioControllerItem;
import com.rks.airdrop.item.WeaponCrateItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RksAirdrops.MODID);

    public static final DeferredItem<Item> AIRDROP_BOX = ITEMS.register(
            "airdrop_box",
            () -> new AirdropBoxItem(ModBlocks.AIRDROP_BOX.get(), itemProperties("airdrop_box").stacksTo(1))
    );

    public static final DeferredItem<Item> AIRDROP = ITEMS.register(
            "airdrop",
            () -> new AirdropItem(itemProperties("airdrop").stacksTo(1))
    );

    public static final DeferredItem<Item> MEDIC_CRATE = ITEMS.register(
            "medic_crate",
            () -> new MedicCrateItem(ModBlocks.MEDIC_CRATE.get(), itemProperties("medic_crate").stacksTo(1))
    );

    public static final DeferredItem<Item> AMMO_CRATE = ITEMS.register(
            "ammo_crate",
            () -> new AmmoCrateItem(ModBlocks.AMMO_CRATE.get(), itemProperties("ammo_crate").stacksTo(1))
    );

    public static final DeferredItem<Item> WEAPON_CRATE = ITEMS.register(
            "weapon_crate",
            () -> new WeaponCrateItem(ModBlocks.WEAPON_CRATE.get(), itemProperties("weapon_crate").stacksTo(1))
    );

    public static final DeferredItem<Item> RADIO_CONTROLLER = ITEMS.register(
            "radio_controller",
            () -> new RadioControllerItem(itemProperties("radio_controller").stacksTo(1))
    );

    public static final DeferredItem<Item> FLARE_GUN = ITEMS.register(
            "flare_gun",
            () -> new FlareGunItem(itemProperties("flare_gun").durability(64))
    );

    private static Item.Properties itemProperties(String name) {
        return new Item.Properties();
    }

    private ModItems() {
    }
}




