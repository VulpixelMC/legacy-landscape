package gay.sylv.legacy_landscape.networking.client_bound;

import gay.sylv.legacy_landscape.codec.LegacyCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public record LegacyChunkPayload(ChunkPos chunkPos, boolean isLegacyChunk) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<LegacyChunkPayload> TYPE = new CustomPacketPayload.Type<>(id("legacy_chunk"));

	public static final StreamCodec<ByteBuf, LegacyChunkPayload> STREAM_CODEC = StreamCodec.composite(
		LegacyCodecs.Stream.CHUNK_POS,
		LegacyChunkPayload::chunkPos,
		ByteBufCodecs.BOOL,
		LegacyChunkPayload::isLegacyChunk,
		LegacyChunkPayload::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static LegacyChunkPayload of(ChunkAccess chunk, boolean isLegacyChunk) {
		return new LegacyChunkPayload(chunk.getPos(), isLegacyChunk);
	}
}
