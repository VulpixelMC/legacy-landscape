package gay.sylv.legacy_landscape.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

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
	}
}
