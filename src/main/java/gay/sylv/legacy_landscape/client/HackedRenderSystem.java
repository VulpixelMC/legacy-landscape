package gay.sylv.legacy_landscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.sylv.legacy_landscape.mixin.client.Accessor_RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public final class HackedRenderSystem {
	private HackedRenderSystem() {}

	public static void setShaderTexture(int shaderTexture, ResourceLocation textureId, boolean blur, boolean mipmap) {
		if (!RenderSystem.isOnRenderThread()) {
			RenderSystem.recordRenderCall(() -> _setShaderTexture(shaderTexture, textureId, blur, mipmap));
		} else {
			_setShaderTexture(shaderTexture, textureId, blur, mipmap);
		}
	}

	private static void _setShaderTexture(int shaderTexture, ResourceLocation textureId, boolean blur, boolean mipmap) {
		int[] shaderTextures = Accessor_RenderSystem.getShaderTextures();
		if (shaderTexture >= 0 && shaderTexture < shaderTextures.length) {
			TextureManager textureManager = LegacyResourceFaker.getTextureManager();
			AbstractTexture abstractTexture = textureManager.getTexture(textureId);
			abstractTexture.setFilter(blur, mipmap);
			shaderTextures[shaderTexture] = abstractTexture.getId();
		}
	}
}
