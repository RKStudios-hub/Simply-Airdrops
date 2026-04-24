package com.rks.airdrop.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.rks.airdrop.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CrateLootManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path LOOT_DIR = FMLPaths.CONFIGDIR.get().resolve("rks_airdrops").resolve("loot");

    private CrateLootManager() {
    }

    public static void ensureDefaults() {
        writeIfMissing("wooden_crate.json", defaultsWooden());
        writeIfMissing("medic_crate.json", defaultsMedic());
        writeIfMissing("ammo_crate.json", defaultsAmmo());
        writeIfMissing("weapon_crate.json", defaultsWeapon());
    }

    public static ItemStack createWoodenCrate(RandomSource random) {
        return fillCrate(new ItemStack(ModItems.AIRDROP_BOX.get()), 27, "wooden_crate.json", random);
    }

    public static ItemStack createMedicCrate(RandomSource random) {
        return fillCrate(new ItemStack(ModItems.MEDIC_CRATE.get()), 9, "medic_crate.json", random);
    }

    public static ItemStack createAmmoCrate(RandomSource random) {
        return fillCrate(new ItemStack(ModItems.AMMO_CRATE.get()), 18, "ammo_crate.json", random);
    }

    public static ItemStack createWeaponCrate(RandomSource random) {
        return fillCrate(new ItemStack(ModItems.WEAPON_CRATE.get()), 9, "weapon_crate.json", random);
    }

    private static ItemStack fillCrate(ItemStack crateStack, int size, String fileName, RandomSource random) {
        LootFile lootFile = readLootFile(fileName);
        NonNullList<ItemStack> contents = NonNullList.withSize(size, ItemStack.EMPTY);
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            slots.add(i);
        }

        for (LootPool pool : lootFile.pools) {
            int rolls = Math.max(0, pool.rolls);
            for (int i = 0; i < rolls && !slots.isEmpty(); i++) {
                ItemStack rolled = rollEntry(pool.entries, random);
                if (rolled.isEmpty()) {
                    continue;
                }

                int slotIndex = random.nextInt(slots.size());
                int slot = slots.remove(slotIndex);
                contents.set(slot, rolled);
            }
        }

        CompoundTag blockEntityTag = new CompoundTag();
        ContainerHelper.saveAllItems(blockEntityTag, contents);
        crateStack.addTagElement("BlockEntityTag", blockEntityTag);
        return crateStack;
    }

    private static ItemStack rollEntry(List<LootEntry> entries, RandomSource random) {
        List<LootEntry> available = entries.stream()
                .filter(entry -> resolveItem(entry.name) != null)
                .filter(entry -> entry.weight > 0)
                .toList();

        if (available.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int totalWeight = available.stream().mapToInt(entry -> entry.weight).sum();
        int pick = random.nextInt(totalWeight);

        for (LootEntry entry : available) {
            pick -= entry.weight;
            if (pick < 0) {
                return createStack(entry, random);
            }
        }

        return ItemStack.EMPTY;
    }

    private static ItemStack createStack(LootEntry entry, RandomSource random) {
        Item item = resolveItem(entry.name);
        if (item == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(item);
        if (entry.nbt != null && !entry.nbt.isBlank()) {
            try {
                stack.setTag(TagParser.parseTag(entry.nbt));
            } catch (Exception ignored) {
            }
        }

        int min = Math.max(1, entry.min_count);
        int max = Math.max(min, entry.max_count);
        int count = entry.count > 0 ? entry.count : (min == max ? min : min + random.nextInt(max - min + 1));
        stack.setCount(Math.min(count, stack.getMaxStackSize()));
        return stack;
    }

    @Nullable
    private static Item resolveItem(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        ResourceLocation location = ResourceLocation.tryParse(id);
        if (location == null) {
            return null;
        }

        return BuiltInRegistries.ITEM.getOptional(location).orElse(null);
    }

    private static LootFile readLootFile(String fileName) {
        ensureDefaults();
        Path path = LOOT_DIR.resolve(fileName);

        try (Reader reader = Files.newBufferedReader(path)) {
            LootFile lootFile = GSON.fromJson(reader, LootFile.class);
            if (lootFile != null && lootFile.pools != null) {
                lootFile.pools.forEach(pool -> {
                    if (pool.entries == null) {
                        pool.entries = List.of();
                    }
                });
                return lootFile;
            }
        } catch (IOException | JsonParseException ignored) {
        }

        return switch (fileName) {
            case "medic_crate.json" -> defaultsMedic();
            case "ammo_crate.json" -> defaultsAmmo();
            case "weapon_crate.json" -> defaultsWeapon();
            default -> defaultsWooden();
        };
    }

    private static void writeIfMissing(String fileName, LootFile lootFile) {
        Path path = LOOT_DIR.resolve(fileName);
        if (Files.exists(path)) {
            return;
        }

        try {
            Files.createDirectories(LOOT_DIR);
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(lootFile, writer);
            }
        } catch (IOException ignored) {
        }
    }

    private static LootFile defaultsWooden() {
        return file(
                pool(12,
                        entry("minecraft:bread", 10, 1, 3),
                        entry("minecraft:cooked_beef", 8, 1, 3),
                        entry("minecraft:apple", 8, 2, 5),
                        entry("minecraft:stick", 10, 4, 12),
                        entry("minecraft:torch", 10, 6, 16),
                        entry("minecraft:stone_sword", 5, 1, 1),
                        entry("minecraft:iron_axe", 3, 1, 1),
                        entry("minecraft:leather_helmet", 4, 1, 1),
                        entry("minecraft:paper", 5, 2, 6),
                        entry("minecraft:compass", 2, 1, 1),
                        entry("minecraft:map", 2, 1, 1),
                        entry("minecraft:experience_bottle", 2, 2, 8),
                        entry("minecraft:string", 7, 2, 6),
                        entry("minecraft:coal", 7, 3, 10),
                        entry("minecraft:iron_ingot", 5, 1, 4),
                        entry("minecraft:golden_carrot", 3, 1, 3),
                        entry("minecraft:bucket", 2, 1, 1),
                        entry("minecraft:name_tag", 1, 1, 1)
                )
        );
    }

    private static LootFile defaultsMedic() {
        return file(
                pool(6,
                        entry("minecraft:potion", 10, 1, 1, "{Potion:\"minecraft:healing\"}"),
                        entry("minecraft:potion", 8, 1, 1, "{Potion:\"minecraft:regeneration\"}"),
                        entry("minecraft:potion", 6, 1, 1, "{Potion:\"minecraft:fire_resistance\"}"),
                        entry("minecraft:potion", 6, 1, 1, "{Potion:\"minecraft:swiftness\"}"),
                        entry("minecraft:splash_potion", 4, 1, 1, "{Potion:\"minecraft:strong_healing\"}"),
                        entry("minecraft:splash_potion", 3, 1, 1, "{Potion:\"minecraft:strong_regeneration\"}"),
                        entry("minecraft:milk_bucket", 5, 1, 1),
                        entry("minecraft:golden_apple", 4, 1, 2),
                        entry("minecraft:enchanted_golden_apple", 1, 1, 1),
                        entry("minecraft:totem_of_undying", 1, 1, 1),
                        entry("minecraft:bandage", 0, 1, 1)
                )
        );
    }

    private static LootFile defaultsAmmo() {
        return file(
                pool(9,
                        entry("tacz:ammo", 16, 20, 48, "{AmmoId:\"tacz:9mm\"}"),
                        entry("tacz:ammo", 12, 18, 40, "{AmmoId:\"tacz:45acp\"}"),
                        entry("tacz:ammo", 14, 16, 36, "{AmmoId:\"tacz:556x45\"}"),
                        entry("tacz:ammo", 12, 16, 32, "{AmmoId:\"tacz:762x39\"}"),
                        entry("tacz:ammo", 8, 8, 20, "{AmmoId:\"tacz:12g\"}"),
                        entry("tacz:ammo", 6, 6, 16, "{AmmoId:\"tacz:308\"}"),
                        entry("tacz:ammo", 2, 2, 6, "{AmmoId:\"tacz:50bmg\"}"),
                        entry("minecraft:arrow", 10, 12, 32),
                        entry("minecraft:spectral_arrow", 4, 4, 12),
                        entry("minecraft:firework_rocket", 5, 2, 8),
                        entry("minecraft:gunpowder", 8, 4, 12)
                )
        );
    }

    private static LootFile defaultsWeapon() {
        return file(
                pool(2,
                        entry("tacz:modern_kinetic_gun", 20, 1, 1, "{GunId:\"tacz:glock_17\"}"),
                        entry("tacz:modern_kinetic_gun", 16, 1, 1, "{GunId:\"tacz:m1911\"}"),
                        entry("tacz:modern_kinetic_gun", 12, 1, 1, "{GunId:\"tacz:uzi\"}"),
                        entry("tacz:modern_kinetic_gun", 10, 1, 1, "{GunId:\"tacz:hk_mp5a5\"}"),
                        entry("tacz:modern_kinetic_gun", 8, 1, 1, "{GunId:\"tacz:ak47\"}"),
                        entry("tacz:modern_kinetic_gun", 6, 1, 1, "{GunId:\"tacz:m4a1\"}"),
                        entry("tacz:modern_kinetic_gun", 4, 1, 1, "{GunId:\"tacz:m870\"}"),
                        entry("minecraft:iron_sword", 4, 1, 1),
                        entry("minecraft:crossbow", 3, 1, 1),
                        entry("minecraft:bow", 3, 1, 1)
                ),
                pool(2,
                        entry("tacz:ammo", 14, 12, 28, "{AmmoId:\"tacz:9mm\"}"),
                        entry("tacz:ammo", 10, 12, 24, "{AmmoId:\"tacz:45acp\"}"),
                        entry("tacz:ammo", 10, 10, 24, "{AmmoId:\"tacz:556x45\"}"),
                        entry("tacz:ammo", 8, 8, 20, "{AmmoId:\"tacz:762x39\"}"),
                        entry("minecraft:arrow", 8, 8, 24)
                ),
                pool(2,
                        entry("tacz:attachment", 8, 1, 1, "{AttachmentId:\"tacz:silencer_light\"}"),
                        entry("tacz:attachment", 8, 1, 1, "{AttachmentId:\"tacz:reflex_sight\"}"),
                        entry("tacz:attachment", 6, 1, 1, "{AttachmentId:\"tacz:light_stock\"}"),
                        entry("tacz:attachment", 6, 1, 1, "{AttachmentId:\"tacz:vertical_grip\"}"),
                        entry("minecraft:spyglass", 2, 1, 1),
                        entry("minecraft:shield", 2, 1, 1)
                )
        );
    }

    private static LootFile file(LootPool... pools) {
        LootFile file = new LootFile();
        file.pools = List.of(pools);
        return file;
    }

    private static LootPool pool(int rolls, LootEntry... entries) {
        LootPool pool = new LootPool();
        pool.rolls = rolls;
        pool.entries = List.of(entries);
        return pool;
    }

    private static LootEntry entry(String name, int weight, int minCount, int maxCount) {
        return entry(name, weight, minCount, maxCount, null);
    }

    private static LootEntry entry(String name, int weight, int minCount, int maxCount, @Nullable String nbt) {
        LootEntry entry = new LootEntry();
        entry.type = "item";
        entry.name = name;
        entry.weight = weight;
        entry.min_count = minCount;
        entry.max_count = maxCount;
        entry.nbt = nbt;
        return entry;
    }

    private static final class LootFile {
        private List<LootPool> pools = List.of();
    }

    private static final class LootPool {
        private int rolls = 1;
        private List<LootEntry> entries = List.of();
    }

    private static final class LootEntry {
        private String type = "item";
        private String name;
        private int weight = 1;
        private int count = 0;
        private int min_count = 1;
        private int max_count = 1;
        private String nbt;
    }
}
