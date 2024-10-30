package gay.sylv.legacy_landscape.util;

/// I'm not British, I just need a non-conflicting name.
public final class Maths {
	public static final int TICK_SECOND = 20;
	public static final int TICK_MINUTE = TICK_SECOND * 60;

	private Maths() {}

	public static int tickMinute(int minutes) {
		return minutes * TICK_MINUTE;
	}
}
