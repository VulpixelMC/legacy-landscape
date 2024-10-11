package gay.sylv.legacy_landscape.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public record BlockItemPair<B extends Block, BI extends BlockItem>(DeferredBlock<B> block, DeferredItem<BI> item) {
}
