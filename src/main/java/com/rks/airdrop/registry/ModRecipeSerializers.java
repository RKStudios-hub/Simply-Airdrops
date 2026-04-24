package com.rks.airdrop.registry;

import com.rks.airdrop.RksAirdrops;
import com.rks.airdrop.recipe.RadioControllerRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RksAirdrops.MODID);

    public static final RegistryObject<RecipeSerializer<RadioControllerRecipe>> RADIO_CONTROLLER_RECIPE =
            RECIPE_SERIALIZERS.register(
                    "radio_controller_crafting",
                    () -> new SimpleCraftingRecipeSerializer<>(RadioControllerRecipe::new)
            );

    private ModRecipeSerializers() {
    }
}
