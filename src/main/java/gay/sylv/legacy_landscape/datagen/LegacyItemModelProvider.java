package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

public final class LegacyItemModelProvider extends ItemModelProvider {
	public LegacyItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		// Items
		basicItem(LegacyItems.ORE_DUST.get());
		withExistingParent(LegacyItems.JAPPAS_WAND.getId().getPath(), mcLoc("blaze_rod"))
			.override()
			.predicate(mcLoc("custom_model_data"), 1)
			.model(getExistingFile(mcLoc("blaze_powder")))
			.end();
		basicItem(LegacyItems.DIAMOND.get());
		handheldItem(LegacyItems.VOID_BUCKET.get());
		basicItem(LegacyItems.TWINE_OF_REALITY.get());
		basicItem(LegacyItems.REALITY_DUST.get());

		// Blocks
		simpleBlockItem(LegacyBlocks.LAZURITE.block().get());
		simpleBlockItem(LegacyBlocks.TURF.block().get());
		simpleBlockItem(LegacyBlocks.COMMAND_BLOCK.block().get());
		simpleBlockItem(LegacyBlocks.FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INVERTED_PATCHED_FABRIC_OF_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.FLOWING_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INVERTED_FLOWING_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INTERTWINED_REALITY.block().get());
		simpleBlockItem(LegacyBlocks.INVERTED_INTERTWINED_REALITY.block().get());
	}
}
