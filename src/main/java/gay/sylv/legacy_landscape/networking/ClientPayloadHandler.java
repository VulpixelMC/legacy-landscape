package gay.sylv.legacy_landscape.networking;

import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.mixin.client.Accessor_ClientLevel;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import gay.sylv.legacy_landscape.networking.client_bound.UnitChunkAttachmentPayload;
import gay.sylv.legacy_landscape.networking.client_bound.UnitEntityAttachmentPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Objects;

public final class ClientPayloadHandler {
	private ClientPayloadHandler() {}

	public static void handleUnitEntityAttachmentPayload(final UnitEntityAttachmentPayload payload, final IPayloadContext context) {
		try {
			ClientLevel level = (ClientLevel) context.player().level();
			Entity entity = Objects.requireNonNull(level.getEntity(payload.entityId()), "entity does not exist");
			@SuppressWarnings("unchecked") AttachmentType<Unit> attachmentType = (AttachmentType<Unit>) NeoForgeRegistries.ATTACHMENT_TYPES.get(payload.attachmentType());
			Objects.requireNonNull(attachmentType);
			if (payload.remove()) {
				entity.removeData(attachmentType);
			} else {
				entity.setData(attachmentType, Unit.INSTANCE);
			}
		} catch (NullPointerException e) {
			LegacyNetworking.LOGGER.error("Failed to decode chunk attachment packet", e);
		}
	}

	public static void handleUnitChunkAttachmentPayload(final UnitChunkAttachmentPayload payload, final IPayloadContext context) {
		try {
			ClientLevel level = (ClientLevel) context.player().level();
			LevelChunk chunk = level.getChunk(payload.chunkPos().x, payload.chunkPos().z);
			@SuppressWarnings("unchecked") AttachmentType<Unit> attachmentType = (AttachmentType<Unit>) NeoForgeRegistries.ATTACHMENT_TYPES.get(payload.attachmentType());
			Objects.requireNonNull(attachmentType);
			if (payload.remove()) {
				chunk.removeData(attachmentType);
			} else {
				chunk.setData(attachmentType, Unit.INSTANCE);
			}
		} catch (NullPointerException e) {
			LegacyNetworking.LOGGER.error("Failed to decode chunk attachment packet", e);
		}
	}

	public static void handleLegacyChunkPayload(final LegacyChunkPayload payload, final IPayloadContext context) {
		ClientLevel level = (ClientLevel) context.player().level();
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
	}
}
