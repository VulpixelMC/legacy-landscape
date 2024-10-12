package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public final class Mixin_ClientLevel {
	@Shadow
	@Final
	private Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;

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
		int tint = original.call(colorResolver, biome, x, z);
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			int humidity = ((tint >> 8) + 0x8) ^ 0xFFFF00FF;
			tint = (tint ^ 0xFF00) | humidity; // clear green channel and set new humidity
		}
		return tint;
	}
}
