package gay.sylv.legacy_landscape.recipe;

import gay.sylv.legacy_landscape.recipe.voids.VoidRecipe;
import gay.sylv.legacy_landscape.recipe.voids.VoidRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;
import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public final class LegacyRecipes {
	public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(
		Registries.RECIPE_TYPE,
		MOD_ID
	);
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
		Registries.RECIPE_SERIALIZER,
		MOD_ID
	);

	public static final DeferredHolder<RecipeType<?>, RecipeType<VoidRecipe>> VOID = registerSimple("void");
	public static final DeferredHolder<RecipeSerializer<?>, VoidRecipeSerializer> VOID_SERIALIZER = SERIALIZERS.register(
		"void",
		VoidRecipeSerializer::new
	);

	private LegacyRecipes() {}

	private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerSimple(String name) {
		return TYPES.register(
			name,
			() -> RecipeType.simple(id(name))
		);
	}
}
