package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
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
	}
}
