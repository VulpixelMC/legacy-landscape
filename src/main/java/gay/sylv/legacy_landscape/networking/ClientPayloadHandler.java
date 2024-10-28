package gay.sylv.legacy_landscape.networking;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.mixin.client.Accessor_ClientLevel;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.io.IOException;

public final class ClientPayloadHandler {
	private ClientPayloadHandler() {}

	public static void handleLegacyChunkPayload(final LegacyChunkPayload payload, final IPayloadContext context) {
		try (ClientLevel level = (ClientLevel) context.player().level()) {
			LevelChunk chunk = level.getChunk(payload.chunkPos().x, payload.chunkPos().z);
			if (payload.chunkType().isPresent()) {
				chunk.setData(LegacyAttachments.LEGACY_CHUNK, payload.chunkType().get());
			} else {
				chunk.removeData(LegacyAttachments.LEGACY_CHUNK);
			}
			((Accessor_ClientLevel) level).getTintCaches().forEach((colorResolver, blockTintCache) -> blockTintCache.invalidateForChunk(chunk.getPos().x, chunk.getPos().z));
			for (int y = level.getMinSection(); y < level.getMaxSection(); y++) {
				Minecraft.getInstance().levelRenderer.setSectionDirty(chunk.getPos().x, y, chunk.getPos().z);
			}
		} catch (IOException e) {
			LegacyNetworking.LOGGER.error("Failed to handle legacy chunk payload", e);
		}
	}
}
