package gay.sylv.legacy_landscape.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

public final class LegacyEffects {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(
		BuiltInRegistries.MOB_EFFECT,
		MOD_ID
	);

	// drinking game idea:
	// try saying evanescence 10 times fast and every time you fail you take a shot and do it again
	public static final DeferredHolder<MobEffect, MobEffect> EVANESCENCE = MOB_EFFECTS.register(
		"evanescence",
		() -> new EvanescenceEffect(MobEffectCategory.HARMFUL, 0xFF181818, ParticleTypes.ASH)
	);

	private LegacyEffects() {}
}
