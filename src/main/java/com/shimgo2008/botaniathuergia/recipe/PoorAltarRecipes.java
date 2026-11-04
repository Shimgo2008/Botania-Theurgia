
package com.shimgo2008.botaniathuergia.recipe;

import com.shimgo2008.botaniathuergia.BotaniaTheurgiaMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PoorAltarRecipes {

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(
                    ForgeRegistries.RECIPE_TYPES,
                    BotaniaTheurgiaMod.MODID
            );

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(
                    ForgeRegistries.RECIPE_SERIALIZERS,
                    BotaniaTheurgiaMod.MODID
            );

    public static final RegistryObject<RecipeType<PoorAltarRecipe>> TYPE =
            TYPES.register("poor_altar", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return BotaniaTheurgiaMod.MODID + ":poor_altar";
                }
            });

    public static final RegistryObject<RecipeSerializer<PoorAltarRecipe>> SERIALIZER =
            SERIALIZERS.register(
                    "poor_altar",
                    PoorAltarRecipeSerializer::new
            );

    private PoorAltarRecipes() {}
}
