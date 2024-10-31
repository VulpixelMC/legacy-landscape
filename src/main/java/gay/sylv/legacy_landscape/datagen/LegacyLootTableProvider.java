package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class LegacyLootTableProvider extends LootTableProvider {
	public LegacyLootTableProvider(
		PackOutput output,
		CompletableFuture<HolderLookup.Provider> provider
	) {
		super(
			output,
			Set.of(),
			List.of(
				new SubProviderEntry(
					BlockLoot::new,
					LootContextParamSets.BLOCK
				)
			),
			provider
		);
	}

	public static final class BlockLoot extends BlockLootSubProvider {
		public BlockLoot(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.DEFAULT_FLAGS, provider);
		}

		@Override
		protected @NotNull Iterable<Block> getKnownBlocks() {
			return LegacyBlocks.BLOCKS.getEntries()
				.stream()
				.map(entry -> (Block) entry.value())
				.toList();
		}

		@Override
		protected void generate() {
			DataProvider.LOGGER.info("{}", getKnownBlocks());
			dropOre(LegacyBlocks.LAZURITE.block().get(), LegacyItems.DIAMOND.get());
			dropSelf(LegacyBlocks.TURF.block().get());
			dropWhenSilkTouch(LegacyBlocks.FABRIC_OF_REALITY.block().get());
			dropSelf(LegacyBlocks.INVERTED_FABRIC_OF_REALITY.block().get());
			dropWhenSilkTouch(LegacyBlocks.EPHEMERAL_FABRIC_OF_REALITY.block().get());
			dropSelf(LegacyBlocks.INVERTED_EPHEMERAL_FABRIC_OF_REALITY.block().get());
			dropWhenSilkTouch(LegacyBlocks.PATCHED_FABRIC_OF_REALITY.block().get());
			dropSelf(LegacyBlocks.INVERTED_PATCHED_FABRIC_OF_REALITY.block().get());
			dropWhenSilkTouch(LegacyBlocks.FLOWING_REALITY.block().get());
			dropSelf(LegacyBlocks.INVERTED_FLOWING_REALITY.block().get());
			dropOre(LegacyBlocks.INTERTWINED_REALITY.block().get(), LegacyItems.TWINE_OF_REALITY.get());
			dropOre(LegacyBlocks.INVERTED_INTERTWINED_REALITY.block().get(), LegacyItems.TWINE_OF_REALITY.get());
		}

		@SuppressWarnings("unused")
		private void noDrop(Block block) {
			this.add(block, noDrop());
		}

		private void dropOre(Block block, Item item) {
			add(
				block,
				createOreDrop(block, item)
			);
		}
	}
}
