package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.AirdropBoxBlockEntity;
import com.rks.airdrop.blockentity.MedicCrateBlockEntity;
import com.rks.airdrop.blockentity.WeaponCrateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RksAirdrops.MODID);

    public static final RegistryObject<BlockEntityType<AirdropBoxBlockEntity>> AIRDROP_BOX =
            BLOCK_ENTITIES.register(
                    "airdrop_box",
                    () -> BlockEntityType.Builder.of(
                            AirdropBoxBlockEntity::new,
                            ModBlocks.AIRDROP_BOX.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<MedicCrateBlockEntity>> MEDIC_CRATE =
            BLOCK_ENTITIES.register(
                    "medic_crate",
                    () -> BlockEntityType.Builder.of(
                            MedicCrateBlockEntity::new,
                            ModBlocks.MEDIC_CRATE.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<WeaponCrateBlockEntity>> WEAPON_CRATE =
            BLOCK_ENTITIES.register(
                    "weapon_crate",
                    () -> BlockEntityType.Builder.of(
                            WeaponCrateBlockEntity::new,
                            ModBlocks.WEAPON_CRATE.get()
                    ).build(null)
            );

    private ModBlockEntities() {
    }
}
