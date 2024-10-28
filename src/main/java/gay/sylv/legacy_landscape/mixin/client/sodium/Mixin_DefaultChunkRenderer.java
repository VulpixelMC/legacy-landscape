package gay.sylv.legacy_landscape.mixin.client.sodium;

import gay.sylv.legacy_landscape.client.util.RenderUtil;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;
import net.caffeinemc.mods.sodium.client.gl.device.RenderDevice;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.caffeinemc.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultChunkRenderer.class)
@Pseudo
public abstract class Mixin_DefaultChunkRenderer extends ShaderChunkRenderer {
	private Mixin_DefaultChunkRenderer(
		RenderDevice device, ChunkVertexType vertexType
	) {
		super(device, vertexType);
	}

	@SuppressWarnings("deprecation")
	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/ShaderChunkRenderer;begin(Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;)V",
			shift = At.Shift.AFTER
		)
	)
	private void setLegacyTextures(
		ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera,
		CallbackInfo ci
	) {
		Minecraft client = Minecraft.getInstance();
		assert client.level != null;
		LevelChunk chunk = client.level.getChunkAt(BlockPos.containing(camera.x, camera.y, camera.z));
		if (chunk.hasData(LegacyAttachments.LEGACY_CHUNK)) {
			RenderUtil.setLegacyTextures(((Accessor_TerrainRenderPass) renderPass).getRenderType());
			this.activeProgram.getInterface().setupState();
		}
	}
}
