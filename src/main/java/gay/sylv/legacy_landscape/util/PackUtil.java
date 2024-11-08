package gay.sylv.legacy_landscape.util;

import gay.sylv.legacy_landscape.api.RuntimeResourcePack;
import gay.sylv.legacy_landscape.datagen.runtime.RuntimeResourcePackImpl;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;

public final class PackUtil {
	private PackUtil() {}

	@Unique
	@Nullable
	public static Pack legacy_landscape$createBuiltinPack(PackType type) {
		return Pack.readMetaAndCreate(RuntimeResourcePack.getInstance().location(), RuntimeResourcePackImpl.FIXED_RESOURCES, type, RuntimeResourcePackImpl.BUILT_IN_SELECTION_CONFIG);
	}
}
