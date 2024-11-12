package gay.sylv.legacy_landscape;

import com.mojang.logging.LogUtils;
import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.data_components.LegacyComponents;
import gay.sylv.legacy_landscape.effect.LegacyEffects;
import gay.sylv.legacy_landscape.fluid.LegacyFluids;
import gay.sylv.legacy_landscape.item.LegacyItems;
import gay.sylv.legacy_landscape.recipe.LegacyRecipes;
import gay.sylv.legacy_landscape.sound.LegacySounds;
import gay.sylv.legacy_landscape.tabs.CreativeTabs;
import gay.sylv.legacy_landscape.util.CallerSensitive;
import gay.sylv.legacy_landscape.util.Constants;
import gay.sylv.legacy_landscape.util.RandomStrings;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Constants.MOD_ID)
public final class LegacyLandscape {
	// Directly reference a slf4j logger
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public LegacyLandscape(IEventBus modBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

		LegacySounds.SOUNDS.register(modBus);
		LegacyComponents.DATA_COMPONENTS.register(modBus);
		LegacyEffects.MOB_EFFECTS.register(modBus);
		LegacyFluids.Types.FLUID_TYPES.register(modBus);
		LegacyFluids.FLUIDS.register(modBus);
		LegacyBlocks.BLOCKS.register(modBus);
		LegacyItems.ITEMS.register(modBus);
		CreativeTabs.CREATIVE_MODE_TABS.register(modBus);
		LegacyAttachments.ATTACHMENT_TYPES.register(modBus);
		LegacyRecipes.TYPES.register(modBus);
		LegacyRecipes.SERIALIZERS.register(modBus);
		modBus.addListener(this::commonSetup);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.error(LogUtils.FATAL_MARKER, RandomStrings.randomString(RandomStrings.STARTUP));
	}

	@CallerSensitive
	public static void init(@NotNull Class<?>... clazzArray) {
		var stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		Class<?> callerClass = stackWalker.getCallerClass();
		if (!callerClass.equals(LegacyLandscape.class)) {
			throw new RuntimeException(new IllegalAccessException("Stop, you are not permitted to call this method! " + callerClass.getCanonicalName() + " is not allowlisted!"));
		}

		try {
			for (Class<?> clazz : clazzArray) {
				LOOKUP.ensureInitialized(clazz);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
