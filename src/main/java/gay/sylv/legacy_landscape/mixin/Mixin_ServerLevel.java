package gay.sylv.legacy_landscape.mixin;

import gay.sylv.legacy_landscape.effect.LegacyEffects;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class Mixin_ServerLevel {
	private Mixin_ServerLevel() {}

	@Inject(
		method = "broadcastEntityEvent",
		at = @At("HEAD"),
		cancellable = true
	)
	private void suppressEvanesced(Entity entity, byte state, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(LegacyEffects.EVANESCENCE)) {
			ci.cancel();
			// If entity is player, only send packet to that player.
			if (entity instanceof ServerPlayer player) {
				player.connection.send(new ClientboundEntityEventPacket(entity, state));
			}
		}
	}
}
