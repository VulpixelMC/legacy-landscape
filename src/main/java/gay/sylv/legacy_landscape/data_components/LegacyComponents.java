package gay.sylv.legacy_landscape.data_components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Broken>> BROKEN = DATA_COMPONENTS.registerComponentType(
		"broken",
		builder -> builder
			.persistent(Broken.CODEC)
			.networkSynchronized(Broken.STREAM_CODEC)
	);

	private LegacyComponents() {}
}
