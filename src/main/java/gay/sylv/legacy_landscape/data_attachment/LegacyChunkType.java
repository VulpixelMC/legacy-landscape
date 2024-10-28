package gay.sylv.legacy_landscape.data_attachment;

import com.mojang.serialization.Codec;
import gay.sylv.legacy_landscape.networking.LegacyNetworking;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public enum LegacyChunkType implements StringRepresentable {
	LEGACY("legacy"),
	DECAYED("decayed");

	public static final Codec<LegacyChunkType> CODEC = StringRepresentable.fromEnum(LegacyChunkType::values);
	public static final StreamCodec<ByteBuf, LegacyChunkType> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public @NotNull LegacyChunkType decode(@NotNull ByteBuf buffer) {
			try {
				String name = new String(FriendlyByteBuf.readByteArray(buffer), StandardCharsets.UTF_8);
				return fromSerializedName(name);
			} catch (DecoderException e) {
				LegacyNetworking.LOGGER.error(e.getLocalizedMessage());
				return LEGACY;
			}
		}

		@Override
		public void encode(@NotNull ByteBuf buffer, @NotNull LegacyChunkType value) {
			FriendlyByteBuf.writeByteArray(buffer, value.getSerializedName().getBytes(StandardCharsets.UTF_8));
		}
	};

	private static final Function<String, LegacyChunkType> NAME_LOOKUP = StringRepresentable.createNameLookup(LegacyChunkType.values(), key -> key);

	private final String name;

	LegacyChunkType(String name) {
		this.name = name;
	}

	@Override
	public @NotNull String getSerializedName() {
		return name;
	}

	public static LegacyChunkType fromSerializedName(String name) {
		return NAME_LOOKUP.apply(name);
	}
}
