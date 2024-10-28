package gay.sylv.legacy_landscape.codec;

import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class LegacyCodecs {
	private LegacyCodecs() {}

	public static final class Stream {
		private Stream() {}

		public static final StreamCodec<ByteBuf, ChunkPos> CHUNK_POS = new StreamCodec<>() {
			@Override
			public @NotNull ChunkPos decode(ByteBuf buffer) {
				return new ChunkPos(buffer.readLong());
			}

			@Override
			public void encode(ByteBuf buffer, ChunkPos chunkPos) {
				buffer.writeLong(chunkPos.toLong());
			}
		};

		public static final StreamCodec<ByteBuf, Optional<LegacyChunkType>> OPTIONAL_CHUNK_TYPE = ofOptional(LegacyChunkType.STREAM_CODEC);

		public static <T> StreamCodec<ByteBuf, Optional<T>> ofOptional(StreamCodec<ByteBuf, T> streamCodec) {
			return new StreamCodec<>() {
				@Override
				public @NotNull Optional<T> decode(@NotNull ByteBuf buffer) {
					if (buffer.readBoolean()) {
						return Optional.of(streamCodec.decode(buffer));
					} else {
						return Optional.empty();
					}
				}

				@Override
				public void encode(@NotNull ByteBuf buffer, @NotNull Optional<T> value) {
					buffer.writeBoolean(value.isPresent());
					value.ifPresent(t -> streamCodec.encode(buffer, t));
				}
			};
		}
	}
}
