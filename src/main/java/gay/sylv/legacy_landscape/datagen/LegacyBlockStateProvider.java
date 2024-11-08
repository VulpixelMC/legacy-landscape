package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;
import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;
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
		simpleUnshaded(LegacyBlocks.FABRIC_OF_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.block().get());
		simpleTranslucentUnshaded(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.block().get());
		simpleTranslucentUnshaded(LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.INVERTED_PATCHED_FABRIC_OF_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.FLOWING_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.INVERTED_FLOWING_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.INTERTWINED_REALITY.block().get());
		simpleUnshaded(LegacyBlocks.INVERTED_INTERTWINED_REALITY.block().get());
	}

	private void simpleTranslucentUnshaded(Block block) {
		ResourceLocation blockLoc = key(block).withPrefix(BLOCK_FOLDER + "/");
		simpleBlock(
			block,
			cubeAllUnshaded(
				name(block),
				blockLoc
			)
				.renderType("translucent")
		);
	}

	private void simpleUnshaded(Block block) {
		ResourceLocation blockLoc = key(block).withPrefix(BLOCK_FOLDER + "/");
		simpleBlock(
			block,
			cubeAllUnshaded(
				name(block),
				blockLoc
			)
		);
	}

	private BlockModelBuilder cubeAllUnshaded(String name, ResourceLocation texture) {
		return models()
			.singleTexture(name, id(BLOCK_FOLDER + "/cube_all_unshaded"), "all", texture);
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
