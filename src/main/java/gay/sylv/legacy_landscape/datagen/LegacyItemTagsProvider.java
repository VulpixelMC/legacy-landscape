package gay.sylv.legacy_landscape.datagen;

import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyItemTagsProvider extends ItemTagsProvider {
	public LegacyItemTagsProvider(
		PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags,
		@Nullable ExistingFileHelper existingFileHelper
	) {
		super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		tag(ItemTags.DURABILITY_ENCHANTABLE)
			.add(LegacyItems.JAPPAS_WAND.asItem());
		tag(ItemTags.VANISHING_ENCHANTABLE)
			.add(LegacyItems.JAPPAS_WAND.asItem());
	}
}
