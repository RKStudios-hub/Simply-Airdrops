package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.block.AirdropBoxBlock;
import com.rks.airdrop.block.AirdropBoxHelperBlock;
import com.rks.airdrop.block.AmmoCrateBlock;
import com.rks.airdrop.block.AmmoCrateHelperBlock;
import com.rks.airdrop.block.MedicCrateBlock;
import com.rks.airdrop.block.MedicCrateHelperBlock;
import com.rks.airdrop.block.WeaponCrateBlock;
import com.rks.airdrop.block.WeaponCrateHelperBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RksAirdrops.MODID);

    public static final DeferredBlock<Block> AIRDROP_BOX = BLOCKS.register(
            "airdrop_box",
            () -> new AirdropBoxBlock(crateProperties("airdrop_box"))
    );

    public static final DeferredBlock<Block> AIRDROP_BOX_HELPER = BLOCKS.register(
            "airdrop_box_helper",
            () -> new AirdropBoxHelperBlock(crateProperties("airdrop_box_helper"))
    );

    public static final DeferredBlock<Block> MEDIC_CRATE = BLOCKS.register(
            "medic_crate",
            () -> new MedicCrateBlock(crateProperties("medic_crate"))
    );

    public static final DeferredBlock<Block> MEDIC_CRATE_HELPER = BLOCKS.register(
            "medic_crate_helper",
            () -> new MedicCrateHelperBlock(crateProperties("medic_crate_helper"))
    );

    public static final DeferredBlock<Block> AMMO_CRATE = BLOCKS.register(
            "ammo_crate",
            () -> new AmmoCrateBlock(crateProperties("ammo_crate"))
    );

    public static final DeferredBlock<Block> AMMO_CRATE_HELPER = BLOCKS.register(
            "ammo_crate_helper",
            () -> new AmmoCrateHelperBlock(crateProperties("ammo_crate_helper"))
    );

    public static final DeferredBlock<Block> WEAPON_CRATE = BLOCKS.register(
            "weapon_crate",
            () -> new WeaponCrateBlock(crateProperties("weapon_crate"))
    );

    public static final DeferredBlock<Block> WEAPON_CRATE_HELPER = BLOCKS.register(
            "weapon_crate_helper",
            () -> new WeaponCrateHelperBlock(crateProperties("weapon_crate_helper"))
    );

    private static BlockBehaviour.Properties crateProperties(String name) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)
                .noOcclusion();
    }

    private ModBlocks() {
    }
}




