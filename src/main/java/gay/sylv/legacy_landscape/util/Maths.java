package gay.sylv.legacy_landscape.util;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.phys.AABB;

/// I'm not British, I just need a non-conflicting name.
public final class Maths {
	public static final int TICK_SECOND = 20;
	public static final int TICK_MINUTE = TICK_SECOND * 60;

	private Maths() {}

	public static int tickMinute(int minutes) {
		return minutes * TICK_MINUTE;
	}

	public static AABB chunkToBox(ChunkPos chunkPos, LevelHeightAccessor level) {
		return chunkToBox(
			chunkPos,
			level.getMinBuildHeight() - 128,
			level.getMaxBuildHeight() + 128
		);
	}

	public static AABB chunkToBox(ChunkPos chunkPos, int minY, int maxY) {
		return new AABB(
			chunkPos.getMinBlockX(),
			minY,
			chunkPos.getMinBlockZ(),
			chunkPos.getMaxBlockX(),
			maxY,
			chunkPos.getMaxBlockZ()
		);
	}
}
