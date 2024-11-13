package gay.sylv.legacy_landscape.recipe.voids;

import gay.sylv.legacy_landscape.api.recipe.builder.VoidRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

/**
 * @deprecated use {@link VoidRecipeBuilder}.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class VoidRecipeBuilderImpl implements RecipeBuilder {
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	private final ItemStack result;
	private String group = "";
	private Ingredient input;

	public VoidRecipeBuilderImpl(Item result, int size) {
		this(new ItemStack(result, size));
	}

	public VoidRecipeBuilderImpl(ItemStack result) {
		this.result = result;
	}

	public static VoidRecipeBuilderImpl of(Item item, int size) {
		return new VoidRecipeBuilderImpl(item, size);
	}

	public static VoidRecipeBuilderImpl of(ItemStack stack) {
		return new VoidRecipeBuilderImpl(stack);
	}

	public void input(Item input) {
		this.input = Ingredient.of(input);
	}

	@Override
	public @NotNull VoidRecipeBuilderImpl unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
		criteria.put(name, criterion);
		return this;
	}

	@Override
	public @NotNull VoidRecipeBuilderImpl group(@Nullable String groupName) {
		group = groupName;
		return this;
	}

	@Override
	public @NotNull Item getResult() {
		return result.getItem();
	}

	@Override
	public void save(@NotNull RecipeOutput recipeOutput) {
		this.save(recipeOutput, RecipeBuilder.getDefaultRecipeId(this.getResult()).withPrefix("void/"));
	}

	@Override
	public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
		Advancement.Builder advancementBuilder = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);
		var recipe = new VoidRecipe(group, input, result);
		recipeOutput
			.accept(
				id,
				recipe,
				advancementBuilder
					.build(id.withPrefix(MOD_ID + "/recipes/void/"))
			);
	}
}
