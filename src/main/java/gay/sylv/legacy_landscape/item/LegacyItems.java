package gay.sylv.legacy_landscape.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

	public static final DeferredItem<OreDustItem> ORE_DUST = registerTooltipItem(
		"ore_dust",
		OreDustItem::new,
		new TooltipItem.Properties()
			.rarity(Rarity.UNCOMMON)
			.stacksTo(99)
			.tooltip(
				Component.translatable("tooltip.legacy_landscape.ore_dust.1")
					.withStyle(
						ChatFormatting.GOLD
					)
			)
			.tooltip(
				TooltipCondition::hasModifierKey,
				Component.translatable("tooltip.legacy_landscape.ore_dust.2")
					.withStyle(
						ChatFormatting.DARK_PURPLE
					)
			)
			.moreInfo()
	);

	public static final DeferredItem<JappasWandItem> JAPPAS_WAND = registerTooltipItem(
		"jappas_wand",
		JappasWandItem::new,
		new TooltipItem.Properties()
			.rarity(Rarity.UNCOMMON)
			.stacksTo(1)
			.tooltip(
				TooltipCondition::hasModifierKey,
				Component.translatable("tooltip.legacy_landscape.jappas_wand.1")
					.withStyle(
						ChatFormatting.DARK_PURPLE
					)
			)
			.moreInfo()
			.durability(300)
	);

	public static final DeferredItem<Item> DIAMOND = ITEMS.registerSimpleItem("diamond");

	private LegacyItems() {}

	private static <I extends TooltipItem> DeferredItem<I> registerTooltipItem(
		String name,
		Function<TooltipItem.Properties, ? extends I> constructor,
		TooltipItem.Properties properties
	) {
		return ITEMS.register(
			name,
			() -> constructor.apply(properties)
		);
	}
}
