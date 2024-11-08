package gay.sylv.legacy_landscape.client;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.sylv.legacy_landscape.mixin.client.Accessor_RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * A cursed utility for fucking with Minecraft's textures using {@link LegacyResourceFaker}'s fake version of {@link TextureManager}. Essentially, this is only used for switching to Programmer Art at runtime.
 */
@OnlyIn(Dist.CLIENT)
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
			TextureManager textureManager = Minecraft.getInstance().getTextureManager();
			AbstractTexture abstractTexture = textureManager.getTexture(textureId);
			abstractTexture.setFilter(blur, mipmap);
			shaderTextures[shaderTexture] = abstractTexture.getId();
		}
	}
}
