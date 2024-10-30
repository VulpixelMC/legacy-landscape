package gay.sylv.legacy_landscape.tabs;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;
import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public final class CreativeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

	public static final Supplier<CreativeModeTab> LEGACY_LANDSCAPE = CREATIVE_MODE_TABS.register(
		MOD_ID,
		() -> CreativeModeTab
			.builder()
			.icon(LegacyBlocks.TURF.item()::toStack)
			.title(Component.translatable("itemGroup.legacy_landscape.legacy_landscape"))
			.displayItems((params, output) -> {
				output.accept(LegacyItems.ORE_DUST);
				output.accept(LegacyItems.JAPPAS_WAND);
				output.accept(LegacyBlocks.TURF.item());
				output.accept(LegacyBlocks.LAZURITE.item());
				output.accept(LegacyItems.DIAMOND);
				output.accept(LegacyItems.VOID_BUCKET);
				output.accept(LegacyItems.TWINE_OF_REALITY);
				output.accept(LegacyBlocks.FABRIC_OF_REALITY.item());
				output.accept(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.item());
				output.accept(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.item());
				output.accept(LegacyBlocks.FLOWING_REALITY.item());
			})
			.build()
	);

	public static final Supplier<CreativeModeTab> LEGACY_LANDSCAPE_CREATIVE = CREATIVE_MODE_TABS.register(
		"creative",
		() -> CreativeModeTab
			.builder()
			.icon(LegacyBlocks.COMMAND_BLOCK.item()::toStack)
			.title(Component.translatable("itemGroup.legacy_landscape.creative"))
			.displayItems((params, output) -> {
				output.accept(LegacyBlocks.COMMAND_BLOCK.item());
			})
			.withTabsBefore(id(MOD_ID))
			.build()
	);

	private CreativeTabs() {}
}
