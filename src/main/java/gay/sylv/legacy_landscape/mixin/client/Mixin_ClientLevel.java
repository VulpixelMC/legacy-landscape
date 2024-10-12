package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public final class Mixin_ClientLevel {
	private Mixin_ClientLevel() {}

	@WrapOperation(
		method = "calculateBlockTint",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"
		)
	)
	private Object disableBiomeBlend(OptionInstance<Integer> instance, Operation<Integer> original, @Local(argsOnly = true) BlockPos blockPos) {
		var self = (ClientLevel) (Object) this;
		LevelChunk chunk = self.getChunkAt(blockPos);
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			return 0;
		} else {
			return original.call(instance);
		}
	}

	@WrapOperation(
		method = "calculateBlockTint",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/ColorResolver;getColor(Lnet/minecraft/world/level/biome/Biome;DD)I"
		)
	)
	private int saturateBlockTint(ColorResolver colorResolver, Biome biome, double x, double z, Operation<Integer> original, @Local(argsOnly = true) BlockPos blockPos) {
		var self = (ClientLevel) (Object) this;
		LevelChunk chunk = self.getChunkAt(blockPos);
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			if (!colorResolver.equals(BiomeColors.WATER_COLOR_RESOLVER)) {
				int tint = original.call(colorResolver, biome, x, z);
				int red = ((tint & 0x00FF0000)) >> 16;
				red += 0x28;
				red = Math.clamp(red, 0, 0x98);
				int green = ((tint & 0x0000FF00)) >> 8;
				green += 0x28;
				green = Math.clamp(green, 0, 0xFF); // prevent overflow/underflow
				tint = (tint & 0xFF0000FF) | green << 8 | red << 16; // clear green channel and set new green
				return tint;
			} else {
				return 0x334FDD;
			}
		}
		return original.call(colorResolver, biome, x, z);
	}
}
