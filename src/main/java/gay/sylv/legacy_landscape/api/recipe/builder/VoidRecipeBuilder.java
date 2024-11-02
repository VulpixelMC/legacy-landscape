package gay.sylv.legacy_landscape.api.recipe.builder;

import gay.sylv.legacy_landscape.recipe.voids.VoidRecipeBuilderImpl;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VoidRecipeBuilder implements RecipeBuilder {
	private VoidRecipeBuilderImpl impl;

	private VoidRecipeBuilder(Item item, int size) {
		this.impl = new VoidRecipeBuilderImpl(item, size);
	}

	private VoidRecipeBuilder(ItemStack result) {
		this.impl = new VoidRecipeBuilderImpl(result);
	}

	public static VoidRecipeBuilder of(Item item, int size) {
		return new VoidRecipeBuilder(item, size);
	}

	public static VoidRecipeBuilder of(Item item) {
		return new VoidRecipeBuilder(item, 1);
	}

	public static VoidRecipeBuilder of(ItemStack stack) {
		return new VoidRecipeBuilder(stack);
	}

	public @NotNull VoidRecipeBuilder input(Item input) {
		impl = impl.input(input);
		return this;
	}

	@Override
	public @NotNull VoidRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
		impl = impl.unlockedBy(name, criterion);
		return this;
	}

	@Override
	public @NotNull VoidRecipeBuilder group(@Nullable String groupName) {
		impl = impl.group(groupName);
		return this;
	}

	@Override
	public @NotNull Item getResult() {
		return impl.getResult();
	}

	@Override
	public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
		impl.save(recipeOutput, id);
	}
}
