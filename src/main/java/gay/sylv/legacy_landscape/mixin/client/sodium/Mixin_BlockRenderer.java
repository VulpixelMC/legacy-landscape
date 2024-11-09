package gay.sylv.legacy_landscape.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.client.util.RenderUtil;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.caffeinemc.mods.sodium.client.model.color.DefaultColorProviders;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import net.caffeinemc.mods.sodium.client.render.texture.SpriteFinderCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Mixin(BlockRenderer.class)
@Pseudo
public abstract class Mixin_BlockRenderer extends AbstractBlockRenderContext {
	@Unique
	private static final ColorProvider<BlockState> SATURATE_GRASS = (levelSlice, blockPos, mutableBlockPos, blockState, modelQuadView, ints) -> Arrays.fill(ints, RenderUtil.saturateTint(
		BiomeColors.getAverageGrassColor(levelSlice, blockPos)));
	@Unique
	private static final ColorProvider<BlockState> SATURATE_FOLIAGE = (levelSlice, blockPos, mutableBlockPos, blockState, modelQuadView, ints) -> Arrays.fill(ints, RenderUtil.saturateTint(BiomeColors.getAverageFoliageColor(levelSlice, blockPos)));
	@Unique
	private static final ColorProvider<BlockState> DESATURATE_GRASS = (levelSlice, blockPos, mutableBlockPos, blockState, modelQuadView, ints) -> Arrays.fill(ints, RenderUtil.desaturateTint(BiomeColors.getAverageGrassColor(levelSlice, blockPos)));
	@Unique
	private static final ColorProvider<BlockState> DESATURATE_FOLIAGE = (levelSlice, blockPos, mutableBlockPos, blockState, modelQuadView, ints) -> Arrays.fill(ints, RenderUtil.desaturateTint(BiomeColors.getAverageFoliageColor(levelSlice, blockPos)));

	private Mixin_BlockRenderer() {}

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
		Optional<LegacyChunkType> chunkType = LegacyAttachments.getChunkData(chunk, LegacyAttachments.LEGACY_CHUNK);
		if (chunkType.isPresent() && colorProvider != null) {
			switch (chunkType.get()) {
				case LEGACY -> {
					if (colorProvider.equals(DefaultColorProviders.GrassColorProvider.BLOCKS)) {
						return SATURATE_GRASS;
					} else if (colorProvider.equals(DefaultColorProviders.FoliageColorProvider.BLOCKS)) {
						return SATURATE_FOLIAGE;
					} else {
						return colorProvider;
					}
				}
				case DECAYED -> {
					if (colorProvider.equals(DefaultColorProviders.GrassColorProvider.BLOCKS)) {
						return DESATURATE_GRASS;
					} else if (colorProvider.equals(DefaultColorProviders.FoliageColorProvider.BLOCKS)) {
						return DESATURATE_FOLIAGE;
					} else {
						return colorProvider;
					}
				}
			}
		}

		return colorProvider;
	}

	/**
	 * Display legacy textures in legacy chunks.
	 */
	@Inject(
		method = "processQuad",
		at = @At("HEAD")
	)
	private void legacyTransformQuad(MutableQuadViewImpl quad, CallbackInfo ci) {
		Minecraft client = Minecraft.getInstance();
		if (Objects.requireNonNull(client.level).getChunkAt(this.pos).hasData(LegacyAttachments.LEGACY_CHUNK)) {
			TextureAtlasSprite sprite = quad.sprite(SpriteFinderCache.forBlockAtlas());
			ResourceLocation key = sprite.contents().name();
			ResourceLocation newKey = key.withPath(key.getPath().replace("block/", "block/legacy/"));
			TextureAtlasSprite newSprite = client.getTextureAtlas(sprite.atlasLocation()).apply(newKey);
			// Override UV
			float diffU0 = newSprite.getU(0.0F) - sprite.getU(0.0F);
			float diffV0 = newSprite.getV(0.0F) - sprite.getV(0.0F);
			float diffU1 = newSprite.getU(1.0F) - sprite.getU(1.0F);
			float diffV1 =  newSprite.getV(1.0F) - sprite.getV(1.0F);
			quad.uv(0, quad.u(0) + diffU0, quad.v(0) + diffV0);
			quad.uv(1, quad.u(1) + diffU0, quad.v(1) + diffV1);
			quad.uv(2, quad.u(2) + diffU1, quad.v(2) + diffV1);
			quad.uv(3, quad.u(3) + diffU1, quad.v(3) + diffV0);
		}
	}
}
