package gay.sylv.legacy_landscape.block;

import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

	private LegacyBlocks() {}

	private static BlockItemPair<Block, BlockItem> registerSimpleBlockItem(String path, BlockBehaviour.Properties properties) {
		var block = BLOCKS.registerSimpleBlock(path, properties);
		return new BlockItemPair<>(
			block,
			LegacyItems.ITEMS.registerSimpleBlockItem(block)
		);
	}
}
