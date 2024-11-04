package gay.sylv.legacy_landscape;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

@Mod(
	value = MOD_ID,
	dist = Dist.CLIENT
)
public final class LegacyLandscapeClient {
	public LegacyLandscapeClient(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
}
