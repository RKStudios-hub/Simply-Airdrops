package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.block.AirdropBoxBlock;
import com.rks.airdrop.block.AirdropBoxHelperBlock;
import com.rks.airdrop.block.MedicCrateBlock;
import com.rks.airdrop.block.MedicCrateHelperBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RksAirdrops.MODID);

    public static final RegistryObject<Block> AIRDROP_BOX = BLOCKS.register(
            "airdrop_box",
            () -> new AirdropBoxBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion())
    );

    public static final RegistryObject<Block> AIRDROP_BOX_HELPER = BLOCKS.register(
            "airdrop_box_helper",
            () -> new AirdropBoxHelperBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion())
    );

    public static final RegistryObject<Block> MEDIC_CRATE = BLOCKS.register(
            "medic_crate",
            () -> new MedicCrateBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion())
    );

    public static final RegistryObject<Block> MEDIC_CRATE_HELPER = BLOCKS.register(
            "medic_crate_helper",
            () -> new MedicCrateHelperBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion())
    );

    private ModBlocks() {
    }
}
