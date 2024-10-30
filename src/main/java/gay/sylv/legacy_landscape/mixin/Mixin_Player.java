package gay.sylv.legacy_landscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import gay.sylv.legacy_landscape.entity.SilentLivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class Mixin_Player extends LivingEntity {
	private Mixin_Player(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(
		method = "playSound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
		)
	)
	private void silenceSound(
		Level instance,
		Player player,
		double x,
		double y,
		double z,
		SoundEvent sound,
		SoundSource category,
		float volume,
		float pitch,
		Operation<Void> original
	) {
		if (!legacy_landscape$isSilentEntity()) {
			original.call(instance, player, x, y, z, sound, category, volume, pitch);
		}
	}

	@WrapOperation(
		method = "playStepSound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;playStepSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
		)
	)
	private void silenceStepSound(
		Player instance,
		BlockPos blockPos,
		BlockState blockState,
		Operation<Void> original
	) {
		if (!legacy_landscape$isSilentEntity()) {
			original.call(instance, blockPos, blockState);
		}
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	@Unique
	private boolean legacy_landscape$isSilentEntity() {
		return (this instanceof SilentLivingEntity silentLivingEntity) && silentLivingEntity.legacy_landscape$isSilent();
	}
}
