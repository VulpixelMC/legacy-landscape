package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.client.HackedRenderSystem;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 1001)
public final class Mixin_LevelRenderer {
	private Mixin_LevelRenderer() {}

	@Inject(
		method = "renderSectionLayer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ShaderInstance;setDefaultUniforms(Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/platform/Window;)V"
		),
		require = 0
	)
	private void setLegacyTextures(RenderType renderType, double x, double y, double z, Matrix4f frustrumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local ShaderInstance shaderInstance) {
		Minecraft client = Minecraft.getInstance();
		assert client.level != null;
		LevelChunk chunk = client.level.getChunkAt(BlockPos.containing(x, y, z));
		if (chunk.getData(LegacyAttachments.LEGACY_CHUNK)) {
			Accessor_CompositeRenderType compositeRenderType = (Accessor_CompositeRenderType) renderType;
			Accessor_CompositeState compositeState = (Accessor_CompositeState) (Object) compositeRenderType.getState();
			assert compositeState != null;
			Accessor_TextureStateShard textureState = (Accessor_TextureStateShard) (compositeState.getTextureState());
			if (textureState.getTexture().isPresent()) {
				HackedRenderSystem.setShaderTexture(0, textureState.getTexture().get(), textureState.isBlur(), textureState.isMipmap());
			}
		}
	}
}
