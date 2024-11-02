package gay.sylv.legacy_landscape.recipe.voids;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record VoidInput(ItemStack stack) implements RecipeInput {
	@Override
	public @NotNull ItemStack getItem(int index) {
		return this.stack();
	}

	@Override
	public int size() {
		return 1;
	}
}
