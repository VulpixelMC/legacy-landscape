package gay.sylv.legacy_landscape.mixin.client.sodium;

import gay.sylv.legacy_landscape.client.util.RenderUtil;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;

@Mixin(ColorProviderRegistry.class)
@Pseudo
public abstract class Mixin_ColorProviderRegistry {
	@Shadow
	protected abstract void registerFluids(ColorProvider<FluidState> provider, Fluid... fluids);

	@Shadow
	public abstract ColorProvider<FluidState> getColorProvider(Fluid fluid);

	private Mixin_ColorProviderRegistry() {}

	@Inject(
		method = "installOverrides",
		at = @At(
			value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/model/color/ColorProviderRegistry;registerFluids(Lnet/caffeinemc/mods/sodium/client/model/color/ColorProvider;[Lnet/minecraft/world/level/material/Fluid;)V",
			shift = At.Shift.AFTER
		)
	)
	private void installWaterOverride(CallbackInfo ci) {
		var colorProvider = this.getColorProvider(Fluids.WATER);
		this.registerFluids((levelSlice, blockPos, mutableBlockPos, fluidState, modelQuadView, ints) -> {
			Minecraft client = Minecraft.getInstance();
			assert client.level != null;
			LevelChunk chunk = client.level.getChunkAt(blockPos);
			Optional<LegacyChunkType> chunkType = LegacyAttachments.getChunkData(chunk, LegacyAttachments.LEGACY_CHUNK);
			switch (chunkType.orElse(null)) {
				case LEGACY -> Arrays.fill(ints, RenderUtil.WATER_COLOR);
				case DECAYED -> Arrays.fill(ints, RenderUtil.DECAYED_WATER_COLOR);
				case null -> colorProvider.getColors(levelSlice, blockPos, mutableBlockPos, fluidState, modelQuadView, ints);
			}
		}, Fluids.WATER, Fluids.FLOWING_WATER);
	}
}
