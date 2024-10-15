package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyBlockStateProvider extends BlockStateProvider {
	public LegacyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(LegacyBlocks.LAZURITE.block().get());
	}
}
