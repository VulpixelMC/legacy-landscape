package gay.sylv.legacy_landscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gay.sylv.legacy_landscape.api.definitions.effect.MobEffects;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.entity.SilentLivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public final class Mixin_Entity {
	private Mixin_Entity() {}

	@WrapOperation(
		method = "isSilent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;"
		)
	)
	private Object isSilent(SynchedEntityData instance, EntityDataAccessor<Boolean> key, Operation<Boolean> original) {
		return original.call(instance, key) || legacy_landscape$isSilentEntity();
	}

	@WrapOperation(
		method = "move",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;getMovementEmission()Lnet/minecraft/world/entity/Entity$MovementEmission;"
		)
	)
	private Entity.MovementEmission silenceMovement(Entity instance, Operation<Entity.MovementEmission> original) {
		if (legacy_landscape$isSilentEntity()) {
			return Entity.MovementEmission.NONE;
		}

		return original.call(instance);
	}

	@Unique
	private boolean legacy_landscape$isSilentEntity() {
		return ((Entity) (Object) this instanceof SilentLivingEntity silentLivingEntity) && silentLivingEntity.legacy_landscape$isSilent();
	}

	@WrapOperation(
		method = "processFlappingMovement",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;getMovementEmission()Lnet/minecraft/world/entity/Entity$MovementEmission;"
		)
	)
	private Entity.MovementEmission silenceFlapping(Entity instance, Operation<Entity.MovementEmission> original) {
		if (legacy_landscape$isSilentEntity()) {
			return Entity.MovementEmission.NONE;
		}

		return original.call(instance);
	}

	@WrapOperation(
		method = "playStepSound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
		)
	)
	private void silenceStepSound(Entity instance, SoundEvent sound, float volume, float pitch, Operation<Void> original) {
		if (!legacy_landscape$isSilentEntity()) {
			original.call(instance, sound, volume, pitch);
		}
	}

	@WrapOperation(
		method = "playMuffledStepSound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
		)
	)
	private void silenceMuffledStepSound(Entity instance, SoundEvent sound, float volume, float pitch, Operation<Void> original) {
		if (!legacy_landscape$isSilentEntity()) {
			original.call(instance, sound, volume, pitch);
		}
	}

	@WrapOperation(
		method = "playCombinationStepSounds",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
		)
	)
	private void silenceCombinationStepSounds(Entity instance, SoundEvent sound, float volume, float pitch, Operation<Void> original) {
		if (!legacy_landscape$isSilentEntity()) {
			original.call(instance, sound, volume, pitch);
		}
	}

	@WrapOperation(
		method = "isInvisibleTo",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;isInvisible()Z"
		)
	)
	private boolean visibleToOmniscient(Entity instance, Operation<Boolean> original, @Local(argsOnly = true) Player player) {
		return original.call(instance) && !player.hasData(LegacyAttachments.OMNISCIENT);
	}

	@WrapOperation(
		method = "isInvisible",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/Entity;getSharedFlag(I)Z"
		)
	)
	private boolean invisibleForEvanesced(Entity instance, int flag, Operation<Boolean> original) {
		return original.call(instance, flag) || (instance instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.EVANESCENCE));
	}
}
