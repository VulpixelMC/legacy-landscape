package gay.sylv.legacy_landscape.block;

import gay.sylv.legacy_landscape.item.LegacyItems;
import gay.sylv.legacy_landscape.sound.LegacySounds;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

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

	private LegacyBlocks() {}

	private static BlockItemPair<Block, BlockItem> registerSimpleBlockItem(String path, BlockBehaviour.Properties properties) {
		var block = BLOCKS.registerSimpleBlock(path, properties);
		return new BlockItemPair<>(
			block,
			LegacyItems.ITEMS.registerSimpleBlockItem(block)
		);
	}
}
