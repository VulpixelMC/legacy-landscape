package gay.sylv.legacy_landscape.effect;

import gay.sylv.legacy_landscape.entity.SilentLivingEntity;
import gay.sylv.legacy_landscape.mixin.Accessor_ChunkMap;
import gay.sylv.legacy_landscape.mixin.Accessor_TrackedEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID
)
public final class EvanescenceEffect extends MobEffect {
	public EvanescenceEffect(MobEffectCategory category, int color, ParticleOptions particle) {
		super(category, color, particle);
	}

	/**
	 * Upon gaining the evanescence effect, all players tracking this {@link LivingEntity} will stop tracking the entity. This creates a vanishing effect as if the entity never existed, effectively making the clients "forget."
	 * @param livingEntity The affected {@link LivingEntity}.
	 * @param amplifier The {@code int amplifier}. This is currently useless.
	 */
	@Override
	public void onEffectAdded(@NotNull LivingEntity livingEntity, int amplifier) {
		super.onEffectAdded(livingEntity, amplifier);
		if (livingEntity.getServer() != null) {
			livingEntity.getServer().execute(() -> {
				Accessor_ChunkMap chunkMap = (Accessor_ChunkMap) ((ServerLevel) livingEntity.level())
					.getChunkSource()
					.chunkMap;
				ChunkMap.TrackedEntity trackedEntity = chunkMap
					.getEntityMap()
					.get(livingEntity.getId());
				// Remove entity from players' clients.
				trackedEntity.broadcastRemoved();
			});
		}
	}

	@Override
	public void fillEffectCures(
		@NotNull Set<EffectCure> cures,
		@NotNull MobEffectInstance effectInstance
	) {
		// No cures.
	}

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		if (event.getEffect().equals(LegacyEffects.EVANESCENCE)) {
			onRemoved(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		if (Objects.requireNonNull(event.getEffectInstance(), "expired effect corresponds to no existing effect instance").getEffect().equals(LegacyEffects.EVANESCENCE)) {
			onRemoved(event.getEntity());
		}
	}

	/**
	 * A function that triggers upon effect removal or expiry, removing the vanishing or evanesced effect from previously affected entities.
	 * @param entity The entity that had Evanescence.
	 */
	private static void onRemoved(LivingEntity entity) {
		if (entity.getServer() != null) {
			entity.getServer().execute(() -> {
				Accessor_ChunkMap chunkMap = (Accessor_ChunkMap) ((ServerLevel) entity.level())
					.getChunkSource()
					.chunkMap;
				ChunkMap.TrackedEntity trackedEntity = chunkMap
					.getEntityMap()
					.get(entity.getId());
				// Tell players to track this entity again.
				//noinspection unchecked
				trackedEntity.updatePlayers((List<ServerPlayer>) entity.level().players());
				((Accessor_TrackedEntity) trackedEntity).getSeenBy()
					.forEach(connection -> connection.send(new ClientboundAddEntityPacket(entity, ((Accessor_TrackedEntity) trackedEntity).getServerEntity())));
			});
		}

		// Make entity no longer silent.
		((SilentLivingEntity) entity).legacy_landscape$setSilent(false);
	}
}
