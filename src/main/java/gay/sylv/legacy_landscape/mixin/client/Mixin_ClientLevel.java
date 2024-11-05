package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.client.util.RenderUtil;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

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
		if (chunk.hasData(LegacyAttachments.LEGACY_CHUNK)) {
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
		Optional<LegacyChunkType> chunkType = LegacyAttachments.getChunkData(chunk, LegacyAttachments.LEGACY_CHUNK);
		final boolean isGrassOrFoliage = colorResolver.equals(BiomeColors.GRASS_COLOR_RESOLVER) || colorResolver.equals(BiomeColors.FOLIAGE_COLOR_RESOLVER);
		final boolean isWater = colorResolver.equals(BiomeColors.WATER_COLOR_RESOLVER);
		switch (chunkType.orElse(null)) {
			case LEGACY -> {
				if (isGrassOrFoliage) {
					int tint = original.call(colorResolver, biome, x, z);
					tint = RenderUtil.saturateTint(tint);
					return tint;
				} else if (isWater) {
					return RenderUtil.WATER_COLOR;
				}
			}
			case DECAYED -> {
				if (isGrassOrFoliage) {
					int tint = original.call(colorResolver, biome, x, z);
					tint = RenderUtil.desaturateTint(tint);
					return tint;
				} else if (isWater) {
					return RenderUtil.DECAYED_WATER_COLOR;
				}
			}
			case null -> {}
		}

		return original.call(colorResolver, biome, x, z);
	}
}
