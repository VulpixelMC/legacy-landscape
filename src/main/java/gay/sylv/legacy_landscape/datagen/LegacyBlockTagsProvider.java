package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.block.LegacyBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyBlockTagsProvider extends BlockTagsProvider {
	public LegacyBlockTagsProvider(
		PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
		@Nullable ExistingFileHelper existingFileHelper
	) {
		super(output, lookupProvider, MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(LegacyBlocks.LAZURITE.block().get());
		tag(BlockTags.NEEDS_STONE_TOOL)
			.add(LegacyBlocks.LAZURITE.block().get());
		tag(BlockTags.MINEABLE_WITH_SHOVEL)
			.add(LegacyBlocks.TURF.block().get());
		tag(BlockTags.NEEDS_STONE_TOOL)
			.add(LegacyBlocks.TURF.block().get());
	}
}
