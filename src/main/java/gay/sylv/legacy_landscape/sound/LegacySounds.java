package gay.sylv.legacy_landscape.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;
import static gay.sylv.legacy_landscape.LegacyLandscape.id;

public final class LegacySounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> DIG_TURF = SOUNDS.register(
		"dig/turf",
		() -> SoundEvent.createVariableRangeEvent(id("dig/turf"))
	);

	private LegacySounds() {}

	public static final class Types {
		public static final DeferredSoundType TURF = new DeferredSoundType(
			1.0f,
			1.0f,
			DIG_TURF,
			DIG_TURF,
			DIG_TURF,
			DIG_TURF,
			DIG_TURF
		);

		private Types() {}
	}
}
