package gay.sylv.legacy_landscape.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import gay.sylv.legacy_landscape.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapelessRecipe.class)
public abstract class Mixin_ShapelessRecipe implements CraftingRecipe {
	private Mixin_ShapelessRecipe() {}

	@ModifyReturnValue(
		method = "getResultItem",
		at = @At("RETURN")
	)
	private ItemStack getResultItem(ItemStack original) {
		Util.modifyDemoItem(original);
		return original;
	}

	@ModifyReturnValue(
		method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
		at = @At("RETURN")
	)
	private ItemStack assemble(ItemStack original) {
		Util.modifyDemoItem(original);
		return original;
	}
}
