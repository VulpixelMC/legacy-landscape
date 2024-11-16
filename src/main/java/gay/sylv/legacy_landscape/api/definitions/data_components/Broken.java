package gay.sylv.legacy_landscape.api.definitions.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;

/**
 * A component that stores how many times an item has broken.
 * @param level the amount of times that something has broken.
 */
@ApiStatus.Experimental
public record Broken(int level) {
	public static final Codec<Broken> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codec.INT.fieldOf("level").forGetter(Broken::level)
		).apply(instance, Broken::new)
	);

	public static final StreamCodec<ByteBuf, Broken> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT,
		Broken::level,
		Broken::new
	);

	public static final Broken UNBROKEN = new Broken(0);

	/**
	 * A {@link Broken} with level {@code -1} that indicates that conditions checking this will always be equal.
	 */
	public static final Broken ALWAYS = new Broken(-1);

	/**
	 * A LUT to avoid duplication.
	 */
	private static final Broken[] LEVELS = new Broken[256];

	static {
		for (int i = 0; i < LEVELS.length; i++) {
			LEVELS[i] = new Broken(i);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Broken broken) {
			return broken.level == this.level || broken.level == -1 || this.level == -1;
		}

		return false;
	}

	/**
	 * Returns a pre-cached value of {@link Broken}. The maximum level is {@code 255}, and the minimum level is {@code 0}.
	 * @param level The level of the {@link Broken}.
	 * @return The corresponding {@link Broken} record.
	 */
	public static Broken of(final int level) {
		assert level < LEVELS.length;
		return LEVELS[level];
	}
}
