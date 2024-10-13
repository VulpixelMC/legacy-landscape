package gay.sylv.legacy_landscape.util;

public final class TintUtil {
	public static final int WATER_COLOR = 0xFF334FDD;

	private TintUtil() {}

	public static int saturateTint(int tint) {
		int red = ((tint & 0x00FF0000)) >> 16;
		red += 0x28;
		red = Math.clamp(red, 0, 0x98);
		int green = ((tint & 0x0000FF00)) >> 8;
		green += 0x28;
		green = Math.clamp(green, 0, 0xFF); // prevent overflow/underflow
		tint = (tint & 0xFF0000FF) | green << 8 | red << 16; // clear green channel and set new green
		return tint;
	}
}
