package com.rks.airdrop;

import com.rks.airdrop.client.AirdropClientEvents;
import com.rks.airdrop.client.AirdropConfigScreenFactory;
import com.rks.airdrop.config.AirdropSettings;
import com.rks.airdrop.loot.CrateLootManager;
import com.rks.airdrop.registry.ModBlockEntities;
import com.rks.airdrop.registry.ModBlocks;
import com.rks.airdrop.registry.ModEntities;
import com.rks.airdrop.registry.ModItems;
import com.rks.airdrop.registry.ModConditions;
import com.rks.airdrop.registry.ModLootModifiers;
import com.rks.airdrop.registry.ModSounds;
import com.rks.airdrop.world.AirdropSpawner;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(RksAirdrops.MODID)
public class RksAirdrops {
    public static final String MODID = "rks_airdrops";
    private static final int[] COUNTDOWN_SECONDS = {60, 30, 10, 5, 4, 3, 2, 1};
    private static final Map<MinecraftServer, Long> NEXT_AIRDROP_TICK = new HashMap<>();
    private static final Map<MinecraftServer, Integer> LAST_ANNOUNCED_SECOND = new HashMap<>();
    private static final Map<MinecraftServer, Long> LAST_INTERVAL_TICKS = new HashMap<>();

    public RksAirdrops(IEventBus modEventBus, ModContainer modContainer) {
        AirdropSettings.load();
        CrateLootManager.ensureDefaults();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModConditions.CONDITIONS.register(modEventBus);

        modEventBus.addListener(ModEvents::addToCreativeTabs);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(AirdropClientEvents::registerRenderers);
        }

        NeoForge.EVENT_BUS.addListener(ForgeEvents::onServerStarted);
        NeoForge.EVENT_BUS.addListener(ForgeEvents::onServerStopped);
        NeoForge.EVENT_BUS.addListener(ForgeEvents::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(ForgeEvents::onServerTickEvent);

        if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("cloth_config")) {
            modContainer.registerExtensionPoint(
                    IConfigScreenFactory.class,
                    (IConfigScreenFactory) (minecraft, parent) -> AirdropConfigScreenFactory.create(parent)
            );
        }
    }

    private static void scheduleNextDrop(MinecraftServer server, long delayTicks) {
        NEXT_AIRDROP_TICK.put(server, server.overworld().getGameTime() + delayTicks);
        LAST_ANNOUNCED_SECOND.remove(server);
    }

    private static void broadcastCountdown(MinecraftServer server, int secondsRemaining) {
        server.getPlayerList().broadcastSystemMessage(
                Component.literal("[Simply Airdrops] ").withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("Airdrop incoming in " + secondsRemaining + " seconds").withStyle(ChatFormatting.GREEN)),
                false
        );
    }

    private static void broadcastDropLocation(MinecraftServer server, AirdropSpawner.SpawnResult result) {
        server.getPlayerList().broadcastSystemMessage(
                Component.literal("[Simply Airdrops] ").withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("Airdrop is dropped at ").withStyle(ChatFormatting.GREEN))
                        .append(Component.literal("X=" + result.spawnPos().getX() + " ").withStyle(ChatFormatting.RED))
                        .append(Component.literal("Y=" + result.spawnPos().getY() + " ").withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("Z=" + result.spawnPos().getZ()).withStyle(ChatFormatting.RED)),
                false
        );
    }

    private static void playTimedAirdropSound(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(player ->
                player.playNotifySound(ModSounds.randomAirplaneSound(player.serverLevel().random), SoundSource.PLAYERS, 2.0F, 1.0F)
        );
    }
    private static void maybeBroadcastCountdown(MinecraftServer server, long gameTime, long nextDropTick) {
        long ticksRemaining = nextDropTick - gameTime;
        int secondsRemaining = (int) ((ticksRemaining + 19L) / 20L);
        Integer lastAnnounced = LAST_ANNOUNCED_SECOND.get(server);

        for (int countdownSecond : COUNTDOWN_SECONDS) {
            if (secondsRemaining == countdownSecond && !Integer.valueOf(countdownSecond).equals(lastAnnounced)) {
                broadcastCountdown(server, countdownSecond);
                LAST_ANNOUNCED_SECOND.put(server, countdownSecond);
                return;
            }
        }
    }

    private static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        if (AirdropSettings.reloadIfChanged()) {
            scheduleNextDrop(server, AirdropSettings.airdropIntervalTicks());
        }

        if (!AirdropSettings.airdropsEnabled()) {
            NEXT_AIRDROP_TICK.remove(server);
            LAST_ANNOUNCED_SECOND.remove(server);
            LAST_INTERVAL_TICKS.remove(server);
            return;
        }

        ServerLevel overworld = server.overworld();
        long interval = AirdropSettings.airdropIntervalTicks();
        Long lastInterval = LAST_INTERVAL_TICKS.get(server);
        if (lastInterval == null || lastInterval.longValue() != interval) {
            scheduleNextDrop(server, interval);
            LAST_INTERVAL_TICKS.put(server, interval);
        }

        long gameTime = overworld.getGameTime();
        long nextDropTick = NEXT_AIRDROP_TICK.computeIfAbsent(server, unused -> gameTime + interval);

        if (interval <= 0L) {
            return;
        }

        if (gameTime < nextDropTick) {
            maybeBroadcastCountdown(server, gameTime, nextDropTick);
            return;
        }

        AirdropSpawner.SpawnResult result = AirdropSpawner.spawnTimedAirdrop(server);
        if (result != null) {
            scheduleNextDrop(server, interval);
            broadcastDropLocation(server, result);
            playTimedAirdropSound(server);
        } else {
            scheduleNextDrop(server, 200L);
        }
    }

    public static class ModEvents {
        public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
                event.accept(ModItems.AIRDROP.get());
                event.accept(ModItems.AIRDROP_BOX.get());
                event.accept(ModItems.MEDIC_CRATE.get());
                event.accept(ModItems.AMMO_CRATE.get());
                event.accept(ModItems.WEAPON_CRATE.get());
            }

            if (event.getTabKey().equals(CreativeModeTabs.TOOLS_AND_UTILITIES)) {
                event.accept(ModItems.RADIO_CONTROLLER.get());
                event.accept(ModItems.FLARE_GUN.get());
            }
        }
    }

    public static class ForgeEvents {
        public static void onServerStarted(ServerStartedEvent event) {
            if (AirdropSettings.airdropsEnabled()) {
                scheduleNextDrop(event.getServer(), AirdropSettings.airdropIntervalTicks());
                LAST_INTERVAL_TICKS.put(event.getServer(), AirdropSettings.airdropIntervalTicks());
            }
        }

        public static void onServerStopped(ServerStoppedEvent event) {
            NEXT_AIRDROP_TICK.remove(event.getServer());
            LAST_ANNOUNCED_SECOND.remove(event.getServer());
            LAST_INTERVAL_TICKS.remove(event.getServer());
        }

        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            MinecraftServer server = event.getEntity().getServer();
            if (server != null && AirdropSettings.airdropsEnabled() && !NEXT_AIRDROP_TICK.containsKey(server)) {
                scheduleNextDrop(server, AirdropSettings.airdropIntervalTicks());
                LAST_INTERVAL_TICKS.put(server, AirdropSettings.airdropIntervalTicks());
            }
        }

        public static void onServerTickEvent(ServerTickEvent.Post event) {
            onServerTick(event);
        }
    }
}





