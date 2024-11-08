package gay.sylv.legacy_landscape.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.id;
import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

public final class LegacySounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> DIG_TURF = registerVariableRange("dig/turf");
	public static final DeferredHolder<SoundEvent, SoundEvent> STEP_TURF = registerVariableRange("step/turf");
	public static final DeferredHolder<SoundEvent, SoundEvent> PLACE_TURF = registerVariableRange("place/turf");
	public static final DeferredHolder<SoundEvent, SoundEvent> HIT_TURF = registerVariableRange("hit/turf");
	public static final DeferredHolder<SoundEvent, SoundEvent> FALL_TURF = registerVariableRange("fall/turf");

	private LegacySounds() {}

	public static final class Types {
		public static final DeferredSoundType TURF = new DeferredSoundType(
			1.0f,
			1.0f,
			DIG_TURF,
			STEP_TURF,
			PLACE_TURF,
			HIT_TURF,
			FALL_TURF
		);

		private Types() {}
	}

	private static DeferredHolder<SoundEvent, SoundEvent> registerVariableRange(String name) {
		return SOUNDS.register(
			name,
			() -> SoundEvent.createVariableRangeEvent(id(name))
		);
	}
}
