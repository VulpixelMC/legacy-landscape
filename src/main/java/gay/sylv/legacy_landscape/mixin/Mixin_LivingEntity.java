package gay.sylv.legacy_landscape.mixin;

import gay.sylv.legacy_landscape.api.definitions.effect.MobEffects;
import gay.sylv.legacy_landscape.entity.SilentLivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class Mixin_LivingEntity extends Entity implements SilentLivingEntity {
	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);

	@Unique
	private boolean legacy_landscape$silent = false;

	private Mixin_LivingEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(
		method = "tick",
		at = @At("HEAD")
	)
	private void silenceIfEvanescent(CallbackInfo ci) {
		this.legacy_landscape$setSilent(this.hasEffect(MobEffects.EVANESCENCE));
	}

	@Override
	public void legacy_landscape$setSilent(boolean silent) {
		this.legacy_landscape$silent = silent;
	}

	@Override
	public boolean legacy_landscape$isSilent() {
		return this.legacy_landscape$silent;
	}
}
