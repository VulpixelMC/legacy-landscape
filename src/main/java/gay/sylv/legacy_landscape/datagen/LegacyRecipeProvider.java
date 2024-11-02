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
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, LegacyItems.JAPPAS_WAND, 1)
			.define('@', LegacyItems.ORE_DUST)
			.define('/', Items.BLAZE_ROD)
			.pattern(" @")
			.pattern("/ ")
			.unlockedBy("has_ore_dust", has(LegacyItems.ORE_DUST))
			.unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyItems.TWINE_OF_REALITY.get())
			.input(Items.STRING)
			.unlockedBy("has_string", has(Items.STRING))
			.save(recipeOutput);
		VoidRecipeBuilder.of(LegacyItems.REALITY_DUST.get())
			.input(LegacyItems.TWINE_OF_REALITY.get())
			.unlockedBy("has_twine_of_reality", has(LegacyItems.TWINE_OF_REALITY.get()))
			.save(recipeOutput);
	}
}
