package gay.sylv.legacy_landscape.util;

import net.minecraft.core.Vec3i;
import org.joml.Vector3f;

public final class Constants {
	public static final Vec3i VOID_COLOR = new Vec3i(24, 24, 24);
	public static final Vector3f VOID_FOG_COLOR = new Vector3f(16.0F * -VOID_COLOR.getX() / 255.0F, 16.0F * -VOID_COLOR.getY() / 255.0F, 16.0F * -VOID_COLOR.getZ() / 255.0F);
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "legacy_landscape";
	public static final String MOD_NAME = "Legacy Landscape";

	private Constants() {}
}
