package com.rks.airdrop.world;

import com.rks.airdrop.config.AirdropSettings;
import com.rks.airdrop.entity.AirdropEntity;
import com.rks.airdrop.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

import java.util.List;

public final class AirdropSpawner {
    private static final int DROP_RADIUS_BLOCKS = 32;

    private AirdropSpawner() {
    }

    public static SpawnResult spawnTimedAirdrop(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers().stream()
                .filter(player -> !player.isSpectator())
                .toList();
        if (players.isEmpty()) {
            return null;
        }

        ServerPlayer target = players.get(server.overworld().random.nextInt(players.size()));
        return spawnNearPlayer(target.serverLevel(), target);
    }

    public static SpawnResult spawnNearPlayer(ServerLevel level, ServerPlayer player) {
        return spawnNearPlayer(level, player, AirdropSettings.airdropSpawnHeight());
    }

    public static SpawnResult spawnNearPlayer(ServerLevel level, ServerPlayer player, int spawnHeight) {
        RandomSource random = level.random;
        int spawnX = player.blockPosition().getX() + random.nextInt(DROP_RADIUS_BLOCKS * 2 + 1) - DROP_RADIUS_BLOCKS;
        int spawnZ = player.blockPosition().getZ() + random.nextInt(DROP_RADIUS_BLOCKS * 2 + 1) - DROP_RADIUS_BLOCKS;
        int spawnY = Math.max(level.getMinBuildHeight() + 32, Math.min(spawnHeight, level.getMaxBuildHeight() - 8));

        AirdropEntity airdrop = ModEntities.AIRDROP.get().create(level);
        if (airdrop == null) {
            return null;
        }

        level.getChunkAt(new BlockPos(spawnX, Math.max(level.getMinBuildHeight(), spawnY - 1), spawnZ));
        airdrop.moveTo(spawnX + 0.5D, spawnY, spawnZ + 0.5D, random.nextFloat() * 360.0F, 0.0F);
        airdrop.setDescending(true);
        if (!level.addFreshEntity(airdrop)) {
            return null;
        }

        return new SpawnResult(level, new BlockPos(spawnX, spawnY, spawnZ));
    }

    public record SpawnResult(ServerLevel level, BlockPos spawnPos) {
    }
}

