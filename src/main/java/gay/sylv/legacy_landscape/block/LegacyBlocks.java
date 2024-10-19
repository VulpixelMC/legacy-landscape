package gay.sylv.legacy_landscape.block;

import gay.sylv.legacy_landscape.item.LegacyItems;
import gay.sylv.legacy_landscape.sound.LegacySounds;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
}
