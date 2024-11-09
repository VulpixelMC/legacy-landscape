package gay.sylv.legacy_landscape;

import gay.sylv.legacy_landscape.client.SuperSecretSetting;
import gay.sylv.legacy_landscape.util.Constants;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(
	modid = Constants.MOD_ID,
	bus = EventBusSubscriber.Bus.MOD
)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue SUPER_SECRET_SETTINGS = BUILDER
		.defineInRange("superSecretSettings", 0, 0, SuperSecretSetting.values().length - 1);
	private static final ModConfigSpec.BooleanValue SHOW_LEGACY_TEXTURES = BUILDER
		.define("showLegacyTextures", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static SuperSecretSetting superSecretSettings;
	public static boolean showLegacyTextures;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
		superSecretSettings = SuperSecretSetting.values()[SUPER_SECRET_SETTINGS.get()];
		Boolean showLegacyTextures1 = SHOW_LEGACY_TEXTURES.get();
		if (showLegacyTextures != showLegacyTextures1) {
			Minecraft client = Minecraft.getInstance();
			client.reloadResourcePacks();
		}
		showLegacyTextures = showLegacyTextures1;
    }
}
