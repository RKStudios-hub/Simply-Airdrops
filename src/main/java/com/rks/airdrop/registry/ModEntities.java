package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.entity.AirdropEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RksAirdrops.MODID);

    public static final RegistryObject<EntityType<AirdropEntity>> AIRDROP = ENTITY_TYPES.register(
            "airdrop",
            () -> EntityType.Builder.<AirdropEntity>of(AirdropEntity::new, MobCategory.MISC)
                    .sized(2.4F, 4.5F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("airdrop")
    );

    private ModEntities() {
    }
}
