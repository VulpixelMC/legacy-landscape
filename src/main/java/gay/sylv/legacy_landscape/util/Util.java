package gay.sylv.legacy_landscape.util;

import gay.sylv.legacy_landscape.ServerConfig;
import gay.sylv.legacy_landscape.item.LegacyItemTags;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Miscellaneous cross-distribution utilities.
 */
public final class Util {
	private static final BlockPredicate ANY_BLOCK = new BlockPredicate(
		Optional.empty(),
		Optional.empty(),
		Optional.empty()
	);

	private Util() {}

	public static void modifyDemoItem(ItemStack original) {
		if (ServerConfig.isDemoMode() && original.is(LegacyItemTags.DEMO_ITEMS)) {
			original
				.set(
					DataComponents.CAN_PLACE_ON,
					new AdventureModePredicate(List.of(ANY_BLOCK), false)
				);
		}
	}
}
