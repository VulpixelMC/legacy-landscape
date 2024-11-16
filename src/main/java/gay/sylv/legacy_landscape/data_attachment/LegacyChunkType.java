package gay.sylv.legacy_landscape.data_attachment;

import com.mojang.serialization.Codec;
import gay.sylv.legacy_landscape.networking.LegacyNetworking;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public enum LegacyChunkType implements StringRepresentable {
	LEGACY("legacy"),
	DECAYED("decayed");

	public static final Codec<LegacyChunkType> CODEC = StringRepresentable.fromEnum(LegacyChunkType::values);
	public static final StreamCodec<FriendlyByteBuf, LegacyChunkType> STREAM_CODEC = StreamCodec.of(
		LegacyChunkType::encode,
		LegacyChunkType::decode
	);

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

	private static void encode(@NotNull FriendlyByteBuf buffer, @NotNull LegacyChunkType value) {
		try {
			buffer.writeUtf(value.getSerializedName());
		} catch (EncoderException e) {
			LegacyNetworking.LOGGER.error(e.getLocalizedMessage());
		}
	}

	private static LegacyChunkType decode(@NotNull FriendlyByteBuf buffer) {
		try {
			return fromSerializedName(buffer.readUtf());
		} catch (DecoderException e) {
			LegacyNetworking.LOGGER.error(e.getLocalizedMessage());
			return LEGACY;
		}
	}
}
