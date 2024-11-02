package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.api.recipe.builder.VoidRecipeBuilder;
import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class LegacyRecipeProvider extends RecipeProvider {
	public LegacyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, LegacyBlocks.TURF.item(), 3)
			.define('~', Items.STRING)
			.define('=', Items.LIME_DYE)
			.define('#', Items.DIRT)
			.pattern("~=~")
			.pattern("~=~")
			.pattern("###")
			.unlockedBy("has_string", has(Items.STRING))
			.unlockedBy("has_lime_dye", has(Items.LIME_DYE))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, LegacyItems.ORE_DUST, 3)
			.requires(Items.GLOWSTONE_DUST)
			.requires(Items.SUGAR)
			.requires(Tags.Items.SEEDS)
			.unlockedBy("has_glowstone_dust", has(Tags.Items.SEEDS))
			.unlockedBy("has_sugar", has(Items.SUGAR))
			.save(recipeOutput);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LegacyItems.JAPPAS_WAND)
			.define('@', LegacyItems.ORE_DUST)
			.define('/', Items.BLAZE_ROD)
			.pattern(" @")
			.pattern("/ ")
			.unlockedBy("has_ore_dust", has(LegacyItems.ORE_DUST))
			.unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, LegacyBlocks.INTERTWINED_REALITY.item(), 2)
			.requires(LegacyBlocks.FABRIC_OF_REALITY.item())
			.requires(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.item())
			.requires(LegacyItems.TWINE_OF_REALITY)
			.requires(Items.STRING)
			.unlockedBy("has_twine_of_reality", has(LegacyItems.TWINE_OF_REALITY))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.item())
			.requires(LegacyBlocks.FABRIC_OF_REALITY.item())
			.requires(LegacyItems.REALITY_DUST)
			.unlockedBy("has_reality_dust", has(LegacyItems.REALITY_DUST))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.item())
			.requires(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.item())
			.requires(LegacyItems.REALITY_DUST)
			.unlockedBy("has_reality_dust", has(LegacyItems.REALITY_DUST))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, LegacyBlocks.FLOWING_REALITY.item())
			.requires(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.item())
			.requires(LegacyItems.TWINE_OF_REALITY)
			.unlockedBy("has_twine_of_reality", has(LegacyItems.TWINE_OF_REALITY))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, LegacyBlocks.INVERTED_FLOWING_REALITY.item())
			.requires(LegacyBlocks.INVERTED_PATCHED_FABRIC_OF_REALITY.item())
			.requires(LegacyItems.TWINE_OF_REALITY)
			.unlockedBy("has_twine_of_reality", has(LegacyItems.TWINE_OF_REALITY))
			.save(recipeOutput);

		// Void recipes
		VoidRecipeBuilder.of(LegacyItems.TWINE_OF_REALITY.get())
			.input(Items.STRING)
			.unlockedBy("has_string", has(Items.STRING))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyItems.REALITY_DUST.get())
			.input(LegacyItems.TWINE_OF_REALITY.get())
			.unlockedBy("has_twine_of_reality", has(LegacyItems.TWINE_OF_REALITY.get()))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyBlocks.FABRIC_OF_REALITY.item().get())
			.input(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.item().get())
			.unlockedBy("has_inverted_fabric_of_reality", has(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.item()))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.item().get())
			.input(LegacyBlocks.FABRIC_OF_REALITY.item().get())
			.unlockedBy("has_fabric_of_reality", has(LegacyBlocks.FABRIC_OF_REALITY.item()))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.item().get())
			.input(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.item().get())
			.unlockedBy("has_ephemeral_fabric_of_reality", has(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.item()))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyBlocks.INVERTED_PATCHED_FABRIC_OF_REALITY.item().get())
			.input(LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.item().get())
			.unlockedBy("has_inverted_ephemeral_fabric_of_reality", has(LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.item()))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyBlocks.INVERTED_INTERTWINED_REALITY.item().get())
			.input(LegacyBlocks.INTERTWINED_REALITY.item().get())
			.unlockedBy("has_intertwined_reality", has(LegacyBlocks.INTERTWINED_REALITY.item()))
			.save(recipeOutput);
	}
}
