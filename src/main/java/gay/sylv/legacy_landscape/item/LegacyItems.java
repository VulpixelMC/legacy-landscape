package gay.sylv.legacy_landscape.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

	public static final DeferredItem<OreDustItem> ORE_DUST = ITEMS.registerItem(
		"ore_dust",
		OreDustItem::new,
		new Item.Properties()
			.rarity(Rarity.UNCOMMON)
			.stacksTo(99)
			.component(
				DataComponents.LORE,
				new ItemLore(
					List.of(
						Component.translatable("lore.legacy_landscape.ore_dust.1"),
						Component.translatable("lore.legacy_landscape.ore_dust.2")
					)
				)
			)
	);

	public static final DeferredItem<NotchWandItem> NOTCH_WAND = ITEMS.registerItem(
		"notch_wand",
		NotchWandItem::new,
		new Item.Properties()
			.rarity(Rarity.UNCOMMON)
			.stacksTo(1)
			.component(
				DataComponents.LORE,
				new ItemLore(
					List.of(
						Component.translatable("lore.legacy_landscape.notch_wand.1")
					)
				)
			)
			.component(
				DataComponents.UNBREAKABLE,
				new Unbreakable(true)
			)
	);

	private LegacyItems() {}
}
