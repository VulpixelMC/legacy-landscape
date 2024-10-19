package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;
import static net.neoforged.neoforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public final class LegacyBlockStateProvider extends BlockStateProvider {
	public LegacyBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(LegacyBlocks.LAZURITE.block().get());
		cubeBottomTop(LegacyBlocks.TURF.block().get());
		simpleBlock(LegacyBlocks.COMMAND_BLOCK.block().get());
	}

	private void cubeBottomTop(Block block) {
		ResourceLocation blockLoc = key(block).withPrefix(BLOCK_FOLDER + "/");
		simpleBlock(
			block,
			models()
				.cubeBottomTop(
					name(block),
					extend(blockLoc, "_side"),
					extend(blockLoc, "_bottom"),
					extend(blockLoc, "_top")
				)
		);
	}

	private ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	private String name(Block block) {
		return key(block).getPath();
	}

	private ResourceLocation extend(ResourceLocation rl, String suffix) {
		return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
	}
}
