package gay.sylv.legacy_landscape.networking.client_bound;

import gay.sylv.legacy_landscape.codec.LegacyCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public record UnitChunkAttachmentPayload(ResourceKey<AttachmentType<?>> attachmentType, ChunkPos chunkPos, boolean remove) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<UnitChunkAttachmentPayload> TYPE = new CustomPacketPayload.Type<>(id("unit_chunk_attachment"));

	public static final StreamCodec<FriendlyByteBuf, UnitChunkAttachmentPayload> STREAM_CODEC = StreamCodec.composite(
		LegacyCodecs.Stream.ATTACHMENT_TYPE,
		UnitChunkAttachmentPayload::attachmentType,
		LegacyCodecs.Stream.CHUNK_POS,
		UnitChunkAttachmentPayload::chunkPos,
		LegacyCodecs.Stream.BOOL,
		UnitChunkAttachmentPayload::remove,
		UnitChunkAttachmentPayload::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
