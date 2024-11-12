package gay.sylv.legacy_landscape;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ServerConfig {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.BooleanValue DEMO_MODE = BUILDER
		.define("demoMode", false);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static boolean isDemoMode() {
		return DEMO_MODE.get();
	}
}
