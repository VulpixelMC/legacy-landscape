package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.client.util.RenderUtil;
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
				tint = RenderUtil.saturateTint(tint);
				return tint;
			} else {
				return RenderUtil.WATER_COLOR;
			}
		}
		return original.call(colorResolver, biome, x, z);
	}
}
