package gay.sylv.legacy_landscape.block;

import gay.sylv.legacy_landscape.fluid.LegacyFluids;
import gay.sylv.legacy_landscape.item.LegacyItems;
import gay.sylv.legacy_landscape.sound.LegacySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

	public static final BlockItemPair<Block, BlockItem> LAZURITE = registerSimpleBlockItem(
		"lazurite",
		BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE) // clearly inspired by its sibling
	);

	public static final BlockItemPair<Block, BlockItem> TURF = registerSimpleBlockItem(
		"turf",
		BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
			.sound(LegacySounds.Types.TURF)
	);

	public static final BlockItemPair<CommandBlock, BlockItem> COMMAND_BLOCK = registerBlockSimpleItem(
		"command_block",
		() -> new CommandBlock(
			BlockBehaviour.Properties.ofFullCopy(Blocks.COMMAND_BLOCK)
		)
	);

	public static final DeferredBlock<LiquidBlock> VOID = BLOCKS.register(
		"void",
		() -> new LiquidBlock(LegacyFluids.VOID_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA).mapColor(MapColor.COLOR_BLACK))
	);

	public static final BlockItemPair<Block, BlockItem> FABRIC_OF_REALITY = registerSimpleBlockItem(
		"fabric_of_reality",
		Properties.fabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> INVERTED_FABRIC_OF_REALITY = registerSimpleBlockItem(
		"inverted_fabric_of_reality",
		Properties.invertedFabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> EPHEMERAL_FABRIC_OF_REALITY = registerSimpleBlockItem(
		"ephemeral_fabric_of_reality",
		Properties.fabricOfReality()
			.noCollission()
	);

	public static final BlockItemPair<Block, BlockItem> INVERTED_EPHEMERAL_FABRIC_OF_REALITY = registerSimpleBlockItem(
		"inverted_ephemeral_fabric_of_reality",
		Properties.invertedFabricOfReality()
			.noCollission()
	);

	public static final BlockItemPair<Block, BlockItem> PATCHED_FABRIC_OF_REALITY = registerSimpleBlockItem(
		"patched_fabric_of_reality",
		Properties.fabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> INVERTED_PATCHED_FABRIC_OF_REALITY = registerSimpleBlockItem(
		"inverted_patched_fabric_of_reality",
		Properties.invertedFabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> FLOWING_REALITY = registerSimpleBlockItem(
		"flowing_reality",
		Properties.fabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> INVERTED_FLOWING_REALITY = registerSimpleBlockItem(
		"inverted_flowing_reality",
		Properties.invertedFabricOfReality()
	);

	public static final BlockItemPair<Block, BlockItem> INTERTWINED_REALITY = registerSimpleBlockItem(
		"intertwined_reality",
		Properties.intertwinedReality()
	);

	public static final BlockItemPair<Block, BlockItem> INVERTED_INTERTWINED_REALITY = registerSimpleBlockItem(
		"inverted_intertwined_reality",
		Properties.intertwinedReality()
	);

	private LegacyBlocks() {}

	private static BlockItemPair<Block, BlockItem> registerSimpleBlockItem(String path, BlockBehaviour.Properties properties) {
		DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(path, properties);
		return new BlockItemPair<>(
			block,
			LegacyItems.ITEMS.registerSimpleBlockItem(block)
		);
	}

	private static <B extends Block> BlockItemPair<B, BlockItem> registerBlockSimpleItem(String path, Supplier<B> blockSupplier) {
		DeferredBlock<B> block = BLOCKS.register(path, blockSupplier);
		return new BlockItemPair<>(
			block,
			LegacyItems.ITEMS.registerSimpleBlockItem(block)
		);
	}

	public static final class Properties {
		private Properties() {}

		public static BlockBehaviour.Properties reality() {
			return BlockBehaviour.Properties.of()
				.strength(0.8F)
				.sound(SoundType.EMPTY) // for an eery silent effect
				.emissiveRendering(Properties::always);
		}

		public static BlockBehaviour.Properties fabricOfReality() {
			return reality()
				.mapColor(MapColor.COLOR_BLACK);
		}

		public static BlockBehaviour.Properties invertedFabricOfReality() {
			return reality()
				.mapColor(MapColor.SNOW);
		}

		public static BlockBehaviour.Properties intertwinedReality() {
			return reality()
				.mapColor(MapColor.COLOR_GRAY);
		}

		private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
			return true;
		}
	}
}
