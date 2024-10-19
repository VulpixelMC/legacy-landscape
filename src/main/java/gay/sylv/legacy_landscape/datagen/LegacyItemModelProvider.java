package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyItemModelProvider extends ItemModelProvider {
	public LegacyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		// Items
		basicItem(LegacyItems.ORE_DUST.get());
		withExistingParent(LegacyItems.JAPPAS_WAND.getId().getPath(), mcLoc("blaze_rod"));
		basicItem(LegacyItems.DIAMOND.get());

		// Blocks
		simpleBlockItem(LegacyBlocks.LAZURITE.block().get());
		simpleBlockItem(LegacyBlocks.TURF.block().get());
		simpleBlockItem(LegacyBlocks.COMMAND_BLOCK.block().get());
	}
}
