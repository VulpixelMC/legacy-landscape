package gay.sylv.legacy_landscape.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * A component that stores how many times an item has broken.
 * @param level the amount of times that something has broken.
 */
public record Broken(int level) {
	public static final Codec<Broken> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.INT.fieldOf("level").forGetter(Broken::level)
		).apply(instance, Broken::new)
	);

	public static final StreamCodec<ByteBuf, Broken> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, Broken::level,
		Broken::new
	);

	public static final Broken UNBROKEN = new Broken(0);
}
