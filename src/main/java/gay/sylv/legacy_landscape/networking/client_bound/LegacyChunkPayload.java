package gay.sylv.legacy_landscape.networking.client_bound;

import gay.sylv.legacy_landscape.codec.LegacyCodecs;
import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public record LegacyChunkPayload(ChunkPos chunkPos, Optional<LegacyChunkType> chunkType) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<LegacyChunkPayload> TYPE = new CustomPacketPayload.Type<>(id("legacy_chunk"));

	public static final StreamCodec<ByteBuf, LegacyChunkPayload> STREAM_CODEC = StreamCodec.composite(
		LegacyCodecs.Stream.CHUNK_POS,
		LegacyChunkPayload::chunkPos,
		LegacyCodecs.Stream.OPTIONAL_CHUNK_TYPE,
		LegacyChunkPayload::chunkType,
		LegacyChunkPayload::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
