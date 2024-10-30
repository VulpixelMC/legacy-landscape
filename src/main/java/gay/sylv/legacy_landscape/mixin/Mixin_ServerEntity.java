package gay.sylv.legacy_landscape.mixin;

import gay.sylv.legacy_landscape.effect.LegacyEffects;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public final class Mixin_ServerEntity {
	@Shadow
	@Final
	private Entity entity;

	private Mixin_ServerEntity() {}

	@Inject(
		method = "addPairing",
		at = @At("HEAD"),
		cancellable = true
	)
	private void hideEvanesced(ServerPlayer player, CallbackInfo ci) {
		if (this.entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(LegacyEffects.EVANESCENCE)) {
			ci.cancel();
		}
	}
}