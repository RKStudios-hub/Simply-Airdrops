package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.entity.AirdropEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, RksAirdrops.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<AirdropEntity>> AIRDROP = ENTITY_TYPES.register(
            "airdrop",
            () -> EntityType.Builder.<AirdropEntity>of(AirdropEntity::new, MobCategory.MISC)
                    .sized(2.75F, 3.5F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("airdrop")
    );

    private ModEntities() {
    }
}




