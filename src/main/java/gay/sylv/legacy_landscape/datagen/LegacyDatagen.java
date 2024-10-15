package gay.sylv.legacy_landscape.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.MOD
)
public final class LegacyDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		addProviders(
			event.includeClient(),
			generator,
			output,
			existingFileHelper,
			LegacyBlockStateProvider::new,
			LegacyItemModelProvider::new
		);

		addProviders(
			event.includeServer(),
			generator,
			new LegacyLootTableProvider(output, lookupProvider),
			new LegacyBlockTagsProvider(output, lookupProvider, existingFileHelper)
		);
	}

	@SafeVarargs
	private static void addProviders(boolean run, DataGenerator generator, PackOutput output, ExistingFileHelper existingFileHelper, BiFunction<PackOutput, ExistingFileHelper, DataProvider>... providers) {
		addProviders(
			run,
			generator,
			Arrays.stream(providers)
				.map(constructor -> constructor.apply(output, existingFileHelper))
				.toArray(DataProvider[]::new)
		);
	}

	private static void addProviders(boolean run, DataGenerator generator, DataProvider... providers) {
		for (DataProvider provider : providers) {
			generator.addProvider(run, provider);
		}
	}
}
