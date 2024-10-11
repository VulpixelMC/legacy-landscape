package gay.sylv.legacy_landscape;

import com.mojang.logging.LogUtils;
import gay.sylv.legacy_landscape.util.RandomStrings;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LegacyLandscape.MOD_ID)
public class LegacyLandscape {
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "legacy_landscape";
	// Directly reference a slf4j logger
	private static final Logger LOGGER = LogUtils.getLogger();

	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public LegacyLandscape(IEventBus modBus, ModContainer modContainer) {
		modBus.addListener(this::commonSetup);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info(RandomStrings.randomString(RandomStrings.STARTUP));
	}
}
