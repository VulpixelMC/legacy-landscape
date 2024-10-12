package gay.sylv.legacy_landscape.networking;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.mixin.ClientLevelAccessor;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.io.IOException;

public final class ClientPayloadHandler {
	private ClientPayloadHandler() {}

	public static void handleLegacyChunkPayload(final LegacyChunkPayload payload, final IPayloadContext context) {
		try (Level level = context.player().level()) {
			LevelChunk chunk = level.getChunk(payload.chunkPos().x, payload.chunkPos().z);
			((ClientLevelAccessor) level).getTintCaches().forEach((colorResolver, blockTintCache) -> blockTintCache.invalidateForChunk(chunk.getPos().x, chunk.getPos().z));
			LegacyNetworking.LOGGER.info("{}", payload);
			if (payload.isLegacyChunk()) {
				chunk.setData(LegacyAttachments.LEGACY_CHUNK, true);
			} else {
				chunk.removeData(LegacyAttachments.LEGACY_CHUNK);
			}
		} catch (IOException e) {
			LegacyNetworking.LOGGER.error("Failed to handle legacy chunk payload", e);
		}
	}
}
