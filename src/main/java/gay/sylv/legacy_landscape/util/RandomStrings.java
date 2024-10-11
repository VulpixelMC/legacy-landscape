package gay.sylv.legacy_landscape.util;

import java.util.Random;

public final class RandomStrings {
	public static final Random RANDOM = new Random();
	public static final String[] STARTUP = new String[]{
		"getting 𝓯𝓻𝓮𝓪𝓴𝔂 👅",
		"injecting hormones into your game",
		"preparing nostalgia…",
		"committing tax fraud :3",
		"try Deltarune!",
		"try Undertale!",
		"inspired by Yttr!",
		"have you tried Ears?",
		"inspired by Minecraft (2011)!",
		"powered by dreams and imagination™",
		"this is gonna be good",
		"exiting the game…",
		"\nhalt\nhalt\nhalt\nhalt\nhalt\nhalt\nhalt",
		"who knew emojis worked in the console 😳",
		"Disconnected: %s",
	};

	private RandomStrings() {}

	public static String randomString(String[] strings) {
		return strings[RANDOM.nextInt(0, strings.length)];
	}
}
