package gay.sylv.legacy_landscape.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Conversions {
	private Conversions() {}

	public static IoSupplier<InputStream> convert(NativeImage image) {
		return new IoSupplier<>() {
			@Override
			public @NotNull InputStream get() throws IOException {
				return new ByteArrayInputStream(image.asByteArray());
			}
		};
	}

	public static IoSupplier<InputStream> convert(String string) {
		return convert(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
	}

	public static IoSupplier<InputStream> convert(InputStream inputStream) {
		return new IoSupplier<>() {
			@Override
			public @NotNull InputStream get() {
				return inputStream;
			}
		};
	}

	public static ChunkPos convert(BlockPos pos) {
		return new ChunkPos(pos.getX() & 15, pos.getZ() & 15);
	}
}
