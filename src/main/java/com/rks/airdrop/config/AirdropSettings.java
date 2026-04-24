package com.rks.airdrop.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public final class AirdropSettings {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("rks_airdrops.json");
    private static Data data = new Data();
    private static FileTime lastModifiedTime;
    private static String lastLoadedContents = "";

    private AirdropSettings() {
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String contents = Files.readString(CONFIG_PATH);
                Data loaded = GSON.fromJson(contents, Data.class);
                data = loaded == null ? new Data() : loaded;
                lastModifiedTime = Files.getLastModifiedTime(CONFIG_PATH);
                lastLoadedContents = contents;
            } catch (IOException | JsonParseException ignored) {
                data = new Data();
                lastLoadedContents = "";
            }
        } else {
            data = new Data();
            lastModifiedTime = null;
            lastLoadedContents = "";
        }

        sanitize();
        writeIfNeeded();
    }

    public static boolean reloadIfChanged() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                if (lastModifiedTime != null) {
                    load();
                    return true;
                }
                return false;
            }

            FileTime currentModifiedTime = Files.getLastModifiedTime(CONFIG_PATH);
            String currentContents = Files.readString(CONFIG_PATH);
            if (lastModifiedTime == null
                    || currentModifiedTime.compareTo(lastModifiedTime) != 0
                    || !currentContents.equals(lastLoadedContents)) {
                load();
                return true;
            }
        } catch (IOException ignored) {
        }

        return false;
    }

    public static void save() {
        sanitize();
        writeIfNeeded();
    }

    private static void writeIfNeeded() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String normalizedContents = GSON.toJson(data);
            if (!Files.exists(CONFIG_PATH) || !normalizedContents.equals(lastLoadedContents)) {
                try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                    writer.write(normalizedContents);
                }
            }
            lastModifiedTime = Files.getLastModifiedTime(CONFIG_PATH);
            lastLoadedContents = Files.readString(CONFIG_PATH);
        } catch (IOException ignored) {
        }
    }

    public static boolean airdropsEnabled() {
        return data.airdropsEnabled;
    }

    public static void setAirdropsEnabled(boolean enabled) {
        data.airdropsEnabled = enabled;
    }

    public static int airdropIntervalSeconds() {
        return data.airdropIntervalSeconds;
    }

    public static void setAirdropIntervalSeconds(int seconds) {
        data.airdropIntervalSeconds = seconds;
    }

    public static long airdropIntervalTicks() {
        return Math.max(60L, data.airdropIntervalSeconds) * 20L;
    }

    public static boolean radioControllerCraftable() {
        return data.radioControllerCraftable;
    }

    public static void setRadioControllerCraftable(boolean craftable) {
        data.radioControllerCraftable = craftable;
    }

    private static void sanitize() {
        if (data.airdropIntervalSeconds < 60) {
            data.airdropIntervalSeconds = 60;
        }
    }

    private static final class Data {
        private boolean airdropsEnabled = true;
        private int airdropIntervalSeconds = 900;
        private boolean radioControllerCraftable = true;
    }
}
