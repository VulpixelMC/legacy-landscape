package gay.sylv.legacy_landscape.codec;

import gay.sylv.legacy_landscape.data_attachment.LegacyChunkType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class LegacyCodecs {
	private LegacyCodecs() {}

	public static final class Stream {
		private Stream() {}

		public static final StreamCodec<ByteBuf, ChunkPos> CHUNK_POS = StreamCodec.of(
			Stream::encodeChunkPos,
			Stream::decodeChunkPos
		);

		public static final StreamCodec<FriendlyByteBuf, ResourceKey<AttachmentType<?>>> ATTACHMENT_TYPE = StreamCodec.of(
			FriendlyByteBuf::writeResourceKey,
			buffer -> buffer.readResourceKey(NeoForgeRegistries.ATTACHMENT_TYPES.key())
		);

		public static final StreamCodec<FriendlyByteBuf, Boolean> BOOL = StreamCodec.of(
			FriendlyByteBuf::writeBoolean,
			FriendlyByteBuf::readBoolean
		);

		public static final StreamCodec<FriendlyByteBuf, Optional<LegacyChunkType>> OPTIONAL_CHUNK_TYPE = ByteBufCodecs.optional(LegacyChunkType.STREAM_CODEC);

		private static void encodeChunkPos(ByteBuf buffer, ChunkPos value) {
			buffer.writeLong(value.toLong());
		}

		private static @NotNull ChunkPos decodeChunkPos(ByteBuf buffer) {
			return new ChunkPos(buffer.readLong());
		}
	}
}
