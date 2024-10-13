package gay.sylv.legacy_landscape.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import net.caffeinemc.mods.sodium.client.util.color.BoxBlur;
import net.caffeinemc.mods.sodium.client.world.biome.LevelColorCache;
import net.caffeinemc.mods.sodium.client.world.cloned.ChunkRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelColorCache.class)
@Pseudo
public final class Mixin_LevelColorCache {
	@Mutable
	@Shadow
	@Final
	private int blendRadius;

	@Unique
	private int legacy_landscape$previousBlendRadius;

	@Unique
	private boolean legacy_landscape$isLegacyChunk;

	private Mixin_LevelColorCache() {}

	@Inject(
		method = "update",
		at = @At("HEAD")
	)
	private void disableBiomeBlending(ChunkRenderContext context, CallbackInfo ci) {
		Minecraft client = Minecraft.getInstance();
		assert client.level != null;
		LevelChunk chunk = client.level.getChunk(context.getOrigin().x(), context.getOrigin().z());
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			legacy_landscape$previousBlendRadius = this.blendRadius;
			this.blendRadius = 0;
			legacy_landscape$isLegacyChunk = true;
		} else {
			legacy_landscape$isLegacyChunk = false;
		}
	}

	@Inject(
		method = "update",
		at = @At("TAIL")
	)
	private void enableBiomeBlending(ChunkRenderContext context, CallbackInfo ci) {
		if (this.blendRadius == 0) {
			this.blendRadius = legacy_landscape$previousBlendRadius;
		}
	}

	@WrapOperation(
		method = "updateColorBuffers",
		at = @At(
			value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/util/color/BoxBlur;blur(Lnet/caffeinemc/mods/sodium/client/util/color/BoxBlur$ColorBuffer;Lnet/caffeinemc/mods/sodium/client/util/color/BoxBlur$ColorBuffer;I)V"
		)
	)
	private void disableBoxBlur(BoxBlur.ColorBuffer buf, BoxBlur.ColorBuffer tmp, int radius, Operation<Void> original) {
		if (!legacy_landscape$isLegacyChunk) {
			original.call(buf, tmp, radius);
		}
	}
}
