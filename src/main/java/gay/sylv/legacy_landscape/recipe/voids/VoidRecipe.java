package gay.sylv.legacy_landscape.recipe.voids;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.fluid.LegacyFluids;
import gay.sylv.legacy_landscape.recipe.LegacyRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID
)
public record VoidRecipe(String group, Ingredient input, ItemStack result) implements Recipe<VoidInput> {
	@SubscribeEvent
	public static void onItemTouchVoid(EntityTickEvent.Post event) {
		if (
			event.getEntity() instanceof ItemEntity entity &&
			entity.isInFluidType(LegacyFluids.Types.VOID.get()) &&
			!entity.hasData(LegacyAttachments.VOID_RESULT) &&
			!entity.level().isClientSide()
		) {
			ItemStack stack = entity.getItem();
			ServerLevel level = (ServerLevel) entity.level();
			RecipeManager recipes = level.getRecipeManager();
			VoidInput input = new VoidInput(stack);
			Optional<RecipeHolder<VoidRecipe>> optionalRecipeHolder = recipes.getRecipeFor(
				LegacyRecipes.VOID.get(),
				input,
				level
			);
			if (optionalRecipeHolder.isPresent()) {
				VoidRecipe recipe = optionalRecipeHolder.get().value();
				entity.setItem(recipe.result);
				entity.setData(LegacyAttachments.VOID_RESULT, Unit.INSTANCE);
			}
		}
	}

	@Override
	public @NotNull NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(input);
	}

	@Override
	public boolean matches(@NotNull VoidInput input, @NotNull Level level) {
		return this.input.test(input.stack());
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull VoidInput input, HolderLookup.@NotNull Provider registries) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width > 0 && height > 0;
	}

	@Override
	public @NotNull String getGroup() {
		return group();
	}

	@Override
	public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
		return this.result;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return LegacyRecipes.VOID_SERIALIZER.get();
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return LegacyRecipes.VOID.get();
	}
}
