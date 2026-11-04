
package com.shimgo2008.botaniathuergia.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class PoorAltarRecipeSerializer
        implements RecipeSerializer<PoorAltarRecipe> {

    @Override
    public PoorAltarRecipe fromJson(
            ResourceLocation id,
            JsonObject json
    ) {
        Ingredient input = Ingredient.fromJson(
                json.get("input")
        );

        ItemStack output = CraftingHelper.getItemStack(
                json.getAsJsonObject("output"),
                true
        );

        return new PoorAltarRecipe(id, input, output);
    }

    @Nullable
    @Override
    public PoorAltarRecipe fromNetwork(
            ResourceLocation id,
            FriendlyByteBuf buffer
    ) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack output = buffer.readItem();

        return new PoorAltarRecipe(id, input, output);
    }

    @Override
    public void toNetwork(
            FriendlyByteBuf buffer,
            PoorAltarRecipe recipe
    ) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
    }
}
