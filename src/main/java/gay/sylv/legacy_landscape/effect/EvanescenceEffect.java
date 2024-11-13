package gay.sylv.legacy_landscape.effect;

import gay.sylv.legacy_landscape.api.definitions.effect.MobEffects;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.mixin.Accessor_ChunkMap;
import gay.sylv.legacy_landscape.mixin.Accessor_TrackedEntity;
import gay.sylv.legacy_landscape.util.Maths;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
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
				((Accessor_TrackedEntity) trackedEntity).getSeenBy()
					.forEach(connection -> {
						ServerPlayer player = connection.getPlayer();
						if (!player.hasData(LegacyAttachments.OMNISCIENT)) {
							((Accessor_TrackedEntity) trackedEntity).getServerEntity().removePairing(player);
						} else {
							sendActiveEffect(livingEntity, connection);
						}
					});
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

	public static void onOmniscient(ServerLevel level, ServerPlayer player) {
		player.getChunkTrackingView().forEach(
			chunkPos -> level.getEntities()
				.get(Maths.chunkToBox(chunkPos, level), entity -> {
					if (entity.equals(player)) return;
					if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.evanescence())) {
						showForPlayer(livingEntity, player);
					}
				})
		);
	}

	public static void onRemoveOmniscience(ServerLevel level, ServerPlayer player) {
		player.getChunkTrackingView().forEach(
			chunkPos -> level.getEntities()
				.get(Maths.chunkToBox(chunkPos, level), entity -> {
					if (entity.equals(player)) return;
					if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(MobEffects.evanescence())) {
						removeForPlayer(livingEntity, player);
					}
				})
		);
	}

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		if (event.getEffect().equals(MobEffects.evanescence())) {
			onRemoved(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		if (Objects.requireNonNull(event.getEffectInstance(), "expired effect corresponds to no existing effect instance").getEffect().equals(MobEffects.evanescence())) {
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
					.forEach(connection -> {
						ServerPlayer player = connection.getPlayer();
						if (player.hasData(LegacyAttachments.OMNISCIENT)) {
							removeInactiveEffect(entity, connection);
						} else {
							pairEntity(player, trackedEntity);
						}
					});
			});
		}
	}

	private static void pairEntity(ServerPlayer player, ChunkMap.TrackedEntity trackedEntity) {
		((Accessor_TrackedEntity) trackedEntity).getServerEntity().addPairing(player);
	}

	private static void showForPlayer(LivingEntity entity, ServerPlayer player) {
		if (entity.getServer() != null) {
			entity.getServer().execute(() -> {
				Accessor_ChunkMap chunkMap = (Accessor_ChunkMap) ((ServerLevel) entity.level())
					.getChunkSource()
					.chunkMap;
				ChunkMap.TrackedEntity trackedEntity = chunkMap
					.getEntityMap()
					.get(entity.getId());
				// Tell player to track this entity again.
				trackedEntity.updatePlayers(List.of(player));
				((Accessor_TrackedEntity) trackedEntity).getSeenBy()
					.forEach(connection -> pairEntity(player, trackedEntity));
				sendActiveEffect(entity, player.connection);
			});
		}
	}

	private static void removeForPlayer(LivingEntity entity, ServerPlayer player) {
		if (entity.getServer() != null) {
			entity.getServer().execute(() -> {
				Accessor_ChunkMap chunkMap = (Accessor_ChunkMap) ((ServerLevel) entity.level())
					.getChunkSource()
					.chunkMap;
				ChunkMap.TrackedEntity trackedEntity = chunkMap
					.getEntityMap()
					.get(entity.getId());
				// Remove entity from player's client.
				((Accessor_TrackedEntity) trackedEntity).getServerEntity().removePairing(player);
			});
		}
	}

	public static void sendActiveEffect(LivingEntity entity, ServerPlayerConnection connection) {
		if (entity.hasEffect(MobEffects.evanescence())) {
			connection.send(
				new ClientboundUpdateMobEffectPacket(
					entity.getId(),
					Objects.requireNonNull(entity.getEffect(MobEffects.evanescence()), "effect corresponds to no existing effect instance"),
					false
				)
			);
		}
	}

	private static void removeInactiveEffect(LivingEntity entity, ServerPlayerConnection connection) {
		connection.send(
			new ClientboundRemoveMobEffectPacket(
				entity.getId(),
				MobEffects.evanescence()
			)
		);
	}
}
