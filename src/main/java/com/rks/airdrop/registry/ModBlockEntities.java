package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.blockentity.AirdropBoxBlockEntity;
import com.rks.airdrop.blockentity.AmmoCrateBlockEntity;
import com.rks.airdrop.blockentity.MedicCrateBlockEntity;
import com.rks.airdrop.blockentity.WeaponCrateBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RksAirdrops.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AirdropBoxBlockEntity>> AIRDROP_BOX =
            BLOCK_ENTITIES.register("airdrop_box", () -> BlockEntityType.Builder.of(AirdropBoxBlockEntity::new, ModBlocks.AIRDROP_BOX.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MedicCrateBlockEntity>> MEDIC_CRATE =
            BLOCK_ENTITIES.register("medic_crate", () -> BlockEntityType.Builder.of(MedicCrateBlockEntity::new, ModBlocks.MEDIC_CRATE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AmmoCrateBlockEntity>> AMMO_CRATE =
            BLOCK_ENTITIES.register("ammo_crate", () -> BlockEntityType.Builder.of(AmmoCrateBlockEntity::new, ModBlocks.AMMO_CRATE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WeaponCrateBlockEntity>> WEAPON_CRATE =
            BLOCK_ENTITIES.register("weapon_crate", () -> BlockEntityType.Builder.of(WeaponCrateBlockEntity::new, ModBlocks.WEAPON_CRATE.get()).build(null));

    private ModBlockEntities() {
    }
}




