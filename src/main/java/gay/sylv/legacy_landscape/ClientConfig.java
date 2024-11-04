package gay.sylv.legacy_landscape;

import gay.sylv.legacy_landscape.client.SuperSecretSetting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = LegacyLandscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue SUPER_SECRET_SETTINGS = BUILDER
		.comment("Super secret settings to enhance your experience.")
		.comment("Do not use this setting if you have photosensitive epilepsy or are otherwise sensitive to flashing lights.")
		.defineInRange("superSecretSettings", 0, 0, SuperSecretSetting.values().length - 1);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static SuperSecretSetting superSecretSettings;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
		superSecretSettings = SuperSecretSetting.values()[SUPER_SECRET_SETTINGS.get()];
    }
}
