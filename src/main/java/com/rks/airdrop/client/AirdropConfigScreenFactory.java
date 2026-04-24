package com.rks.airdrop.client;

import com.rks.airdrop.config.AirdropSettings;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AirdropConfigScreenFactory {
    private AirdropConfigScreenFactory() {
    }

    public static Screen create(Screen parent) {
        AirdropSettings.load();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.rks_airdrops.title"))
                .setSavingRunnable(AirdropSettings::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.rks_airdrops.category.general"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        general.addEntry(entries.startBooleanToggle(
                        Component.translatable("config.rks_airdrops.airdrops_enabled"),
                        AirdropSettings.airdropsEnabled())
                .setDefaultValue(true)
                .setSaveConsumer(AirdropSettings::setAirdropsEnabled)
                .build());

        general.addEntry(entries.startIntField(
                        Component.translatable("config.rks_airdrops.airdrop_interval_seconds"),
                        AirdropSettings.airdropIntervalSeconds())
                .setDefaultValue(900)
                .setMin(60)
                .setSaveConsumer(AirdropSettings::setAirdropIntervalSeconds)
                .build());

        general.addEntry(entries.startBooleanToggle(
                        Component.translatable("config.rks_airdrops.radio_controller_craftable"),
                        AirdropSettings.radioControllerCraftable())
                .setDefaultValue(true)
                .setSaveConsumer(AirdropSettings::setRadioControllerCraftable)
                .build());

        return builder.build();
    }
}
