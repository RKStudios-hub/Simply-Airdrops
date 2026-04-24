package com.rks.airdrop.recipe;

import com.rks.airdrop.config.AirdropSettings;
import com.rks.airdrop.registry.ModItems;
import com.rks.airdrop.registry.ModRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RadioControllerRecipe extends CustomRecipe {
    public RadioControllerRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (!AirdropSettings.radioControllerCraftable() || container.getWidth() != 3 || container.getHeight() != 3) {
            return false;
        }

        return container.getItem(0).is(Items.IRON_INGOT)
                && container.getItem(1).is(Items.REDSTONE)
                && container.getItem(2).is(Items.IRON_INGOT)
                && container.getItem(3).is(Items.COPPER_INGOT)
                && container.getItem(4).is(Items.ENDER_PEARL)
                && container.getItem(5).is(Items.COPPER_INGOT)
                && container.getItem(6).is(Items.IRON_INGOT)
                && container.getItem(7).is(Items.REDSTONE)
                && container.getItem(8).is(Items.IRON_INGOT);
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return new ItemStack(ModItems.RADIO_CONTROLLER.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(ModItems.RADIO_CONTROLLER.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.RADIO_CONTROLLER_RECIPE.get();
    }
}
