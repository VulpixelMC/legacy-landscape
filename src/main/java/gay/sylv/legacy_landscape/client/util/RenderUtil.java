package gay.sylv.legacy_landscape.client.util;

import gay.sylv.legacy_landscape.client.HackedRenderSystem;
import gay.sylv.legacy_landscape.mixin.client.Accessor_CompositeRenderType;
import gay.sylv.legacy_landscape.mixin.client.Accessor_CompositeState;
import gay.sylv.legacy_landscape.mixin.client.Accessor_TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * A grab-bag of utilities used in legacy-style rendering.
 */
@OnlyIn(Dist.CLIENT)
public final class RenderUtil {
	public static final int WATER_COLOR = 0xFF334FDD;
	public static final int DECAYED_WATER_COLOR = 0xFF425CB4;
	public static final Vec3i VOID_COLOR = new Vec3i(24, 24, 24);

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

	// https://stackoverflow.com/questions/687261/converting-rgb-to-grayscale-intensity#689547
	public static int desaturateTint(int tint) {
		int red = (tint & 0x00FF0000) >> 16;
		int green = (tint & 0x0000FF00) >> 8;
		int blue = (tint & 0x000000FF);
		red += green + blue;
		red /= 3;
		red = Math.max(red, 0);
		green += red + blue;
		green = Mth.floor(green / 2.5f);
		green = Math.max(green, 0); // prevent overflow/underflow
		blue += red + green;
		blue /= 3;
		blue = Math.max(blue, 0); // prevent overflow/underflow
		tint = (tint & 0xFF000000) | blue | green << 8 | red << 16; // clear green channel and set new green
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
