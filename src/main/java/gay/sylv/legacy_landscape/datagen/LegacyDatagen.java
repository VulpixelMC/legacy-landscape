package gay.sylv.legacy_landscape.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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

		generator.addProvider(
			event.includeClient(),
			new LegacyItemModelProvider(output, existingFileHelper)
		);
	}
}
