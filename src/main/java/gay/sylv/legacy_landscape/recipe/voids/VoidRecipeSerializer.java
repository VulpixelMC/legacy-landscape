package gay.sylv.legacy_landscape.recipe.voids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public final class VoidRecipeSerializer implements RecipeSerializer<VoidRecipe> {
	private static final MapCodec<VoidRecipe> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			Codec.STRING.fieldOf("group").forGetter(VoidRecipe::group),
			Ingredient.CODEC.fieldOf("ingredient").forGetter(VoidRecipe::input),
			ItemStack.CODEC.fieldOf("result").forGetter(VoidRecipe::result)
		).apply(instance, VoidRecipe::new)
	);
	private static final StreamCodec<RegistryFriendlyByteBuf, VoidRecipe> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8,
		VoidRecipe::group,
		Ingredient.CONTENTS_STREAM_CODEC,
		VoidRecipe::input,
		ItemStack.STREAM_CODEC,
		VoidRecipe::result,
		VoidRecipe::new
	);

	@Override
	public @NotNull MapCodec<VoidRecipe> codec() {
		return CODEC;
	}

	@Override
	public @NotNull StreamCodec<RegistryFriendlyByteBuf, VoidRecipe> streamCodec() {
		return STREAM_CODEC;
	}
}
