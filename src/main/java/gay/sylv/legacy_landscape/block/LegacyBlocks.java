package gay.sylv.legacy_landscape.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyBlocks {
	private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

	private static BlockItemPair<Block, BlockItem> registerSimpleBlockItem(String path, BlockBehaviour.Properties properties) {
		var block = BLOCKS.registerSimpleBlock(path, properties);
		return new BlockItemPair<>(
			block,
			ITEMS.registerSimpleBlockItem(block)
		);
	}
}
