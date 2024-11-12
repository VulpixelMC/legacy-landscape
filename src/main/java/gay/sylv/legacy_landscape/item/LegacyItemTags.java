package gay.sylv.legacy_landscape.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public final class LegacyItemTags {
	public static final TagKey<Item> DEMO_ITEMS = TagKey.create(
		BuiltInRegistries.ITEM.key(),
		id("demo_items")
	);
}
