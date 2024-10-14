package gay.sylv.legacy_landscape.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.client.util.RenderUtil;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.caffeinemc.mods.sodium.client.model.color.DefaultColorProviders;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(BlockRenderer.class)
@Pseudo
public class Mixin_BlockRenderer {
	@WrapOperation(
		method = "renderModel",
		at = @At(
			value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/model/color/ColorProviderRegistry;getColorProvider(Lnet/minecraft/world/level/block/Block;)Lnet/caffeinemc/mods/sodium/client/model/color/ColorProvider;"
		)
	)
	private ColorProvider<BlockState> hijackColorProvider(ColorProviderRegistry instance, Block block, Operation<ColorProvider<BlockState>> original, @Local(argsOnly = true, ordinal = 0) BlockPos pos) {
		Minecraft client = Minecraft.getInstance();
		assert client.level != null;
		LevelChunk chunk = client.level.getChunkAt(pos);
		ColorProvider<BlockState> colorProvider = original.call(instance, block);
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			return (levelSlice, blockPos, mutableBlockPos, blockState, modelQuadView, ints) -> {
				if (colorProvider.equals(DefaultColorProviders.GrassColorProvider.BLOCKS)) {
					Arrays.fill(ints, RenderUtil.saturateTint(BiomeColors.getAverageGrassColor(levelSlice, blockPos)));
				} else if (colorProvider.equals(DefaultColorProviders.FoliageColorProvider.BLOCKS)) {
					Arrays.fill(ints, RenderUtil.saturateTint(BiomeColors.getAverageFoliageColor(levelSlice, blockPos)));
				}
			};
		} else {
			return colorProvider;
		}
	}
}
