package gay.sylv.legacy_landscape.client.util;

import gay.sylv.legacy_landscape.client.HackedRenderSystem;
import gay.sylv.legacy_landscape.mixin.client.Accessor_CompositeRenderType;
import gay.sylv.legacy_landscape.mixin.client.Accessor_CompositeState;
import gay.sylv.legacy_landscape.mixin.client.Accessor_TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * A grab-bag of utilities used in legacy-style rendering.
 */
@OnlyIn(Dist.CLIENT)
public final class RenderUtil {
	public static final int WATER_COLOR = 0xFF334FDD;

	private RenderUtil() {}

	public static int saturateTint(int tint) {
		int red = (tint & 0x00FF0000) >> 16;
		red += 0x28;
		red = Math.clamp(red, 0, 0x98);
		int green = (tint & 0x0000FF00) >> 8;
		green += 0x28;
		green = Math.clamp(green, 0, 0xFF); // prevent overflow/underflow
		tint = (tint & 0xFF0000FF) | green << 8 | red << 16; // clear green channel and set new green
		return tint;
	}

	public static void setLegacyTextures(RenderType renderType) {
		Accessor_CompositeRenderType compositeRenderType = (Accessor_CompositeRenderType) renderType;
		Accessor_CompositeState compositeState = (Accessor_CompositeState) (Object) compositeRenderType.getState();
		assert compositeState != null;
		Accessor_TextureStateShard textureState = (Accessor_TextureStateShard) (compositeState.getTextureState());
		if (textureState.getTexture().isPresent()) {
			HackedRenderSystem.setShaderTexture(0, textureState.getTexture().get(), textureState.isBlur(), textureState.isMipmap());
		}
	}
}
