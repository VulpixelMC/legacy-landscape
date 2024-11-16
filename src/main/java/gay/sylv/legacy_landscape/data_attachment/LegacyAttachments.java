package gay.sylv.legacy_landscape.data_attachment;

import gay.sylv.legacy_landscape.effect.EvanescenceEffect;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import gay.sylv.legacy_landscape.networking.client_bound.UnitChunkAttachmentPayload;
import gay.sylv.legacy_landscape.networking.client_bound.UnitEntityAttachmentPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.GAME
)
public final class LegacyAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

	public static final Supplier<AttachmentType<LegacyChunkType>> LEGACY_CHUNK = ATTACHMENT_TYPES.register(
		"legacy_chunk",
		() -> AttachmentType
			.builder(() -> LegacyChunkType.LEGACY)
			.serialize(LegacyChunkType.CODEC)
			.build()
	);

	public static final Supplier<AttachmentType<Unit>> VOID_RESULT = ATTACHMENT_TYPES.register(
		"void_result",
		() -> AttachmentType
			.builder(() -> Unit.INSTANCE)
			.serialize(Unit.CODEC)
			.build()
	);

	/**
	 * Allows adventure mode players to interact with the chunk's legacy status.
	 */
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> ALLOW_ADVENTURE_MODE = ATTACHMENT_TYPES.register(
		"allow_adventure_mode",
		() -> AttachmentType
			.builder(() -> Unit.INSTANCE)
			.serialize(Unit.CODEC)
			.build()
	);

	/**
	 * Allows operators to see evanesced entities.
	 */
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> OMNISCIENT = ATTACHMENT_TYPES.register(
		"omniscient",
		() -> AttachmentType
			.builder(() -> Unit.INSTANCE)
			.serialize(Unit.CODEC)
			.copyOnDeath()
			.build()
	);

	private LegacyAttachments() {}

	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player && player.hasData(LegacyAttachments.OMNISCIENT)) {
			PacketDistributor.sendToPlayer(
				player,
				new UnitEntityAttachmentPayload(OMNISCIENT.getKey(), player.getId(), false)
			);
		}
	}

	/**
	 * Makes a player omniscient.
	 * @param player the player to modify.
	 */
	public static void setOmniscient(ServerPlayer player) {
		player.setData(OMNISCIENT, Unit.INSTANCE);
		PacketDistributor.sendToPlayer(
			player,
			new UnitEntityAttachmentPayload(OMNISCIENT.getKey(), player.getId(), false)
		);
		EvanescenceEffect.onOmniscient((ServerLevel) player.level(), player);
	}

	/**
	 * Removes a player's omniscience.
	 * @param player the player to modify.
	 */
	public static void removeOmniscience(ServerPlayer player) {
		player.removeData(OMNISCIENT);
		PacketDistributor.sendToPlayer(
			player,
			new UnitEntityAttachmentPayload(OMNISCIENT.getKey(), player.getId(), true)
		);
		EvanescenceEffect.onRemoveOmniscience((ServerLevel) player.level(), player);
	}

	/**
	 * A version of {@link ChunkAccess#getData(AttachmentType)} that returns {@link Optional#empty()} when no data is present.
	 * @param chunk The chunk to get the data from.
	 * @param attachmentType The {@link AttachmentType}.
	 * @return The data (if it exists) {@link Optional}&lt;{@link T}&gt;.
	 * @param <T> The type of data to get.
	 */
	public static <T> Optional<T> getChunkData(ChunkAccess chunk, Supplier<AttachmentType<T>> attachmentType) {
		return Optional.ofNullable(chunk.hasData(attachmentType) ? chunk.getData(attachmentType) : null);
	}

	/**
	 * A version of {@link ChunkAccess#setData(AttachmentType, Object)} that additionally synchronizes the chunk data with clients.
	 * @param level The level in which the chunk resides.
	 * @param chunk The chunk to set data in.
	 * @param attachmentType The {@link AttachmentType}.
	 * @param data The data {@link T}.
	 * @param payloadSupplier A supplier that instantiates a packet {@link P}.
	 * @return The previous data (if it exists) {@link Optional}&lt;{@link T}&gt;.
	 * @param <T> The type of data to store.
	 */
	public static <T, P extends CustomPacketPayload> Optional<T> setChunkData(ServerLevel level, ChunkAccess chunk, Supplier<AttachmentType<T>> attachmentType, T data, Function<T, P> payloadSupplier) {
		PacketDistributor.sendToPlayersTrackingChunk(level, chunk.getPos(), payloadSupplier.apply(data));
		return Optional.ofNullable(chunk.setData(attachmentType, data));
	}

	/**
	 * A version of {@link ChunkAccess#setData(AttachmentType, Object)} that additionally synchronizes the chunk data with clients. This method works with {@link AttachmentType}s with {@link Unit} data.
	 * @param level The level in which the chunk resides.
	 * @param chunk The chunk to set data in.
	 * @param attachmentType The {@link AttachmentType}.
	 */
	public static void setChunkData(ServerLevel level, ChunkAccess chunk, DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> attachmentType) {
		PacketDistributor.sendToPlayersTrackingChunk(
			level,
			chunk.getPos(),
			new UnitChunkAttachmentPayload(attachmentType.getKey(), chunk.getPos(), false)
		);
		chunk.setData(attachmentType, Unit.INSTANCE);
	}

	/**
	 * A version of {@link ChunkAccess#removeData(AttachmentType)} that additionally synchronizes the chunk data with clients.
	 * @param level The level in which the chunk resides.
	 * @param chunk The chunk to set data in.
	 * @param attachmentType The {@link AttachmentType}.
	 * @param payloadSupplier A supplier that instantiates a packet {@link P}.
	 * @return The previous data (if it exists) {@link Optional}&lt;{@link T}&gt;.
	 * @param <T> The type of data to store.
	 */
	public static <T, P extends CustomPacketPayload> Optional<T> removeChunkData(ServerLevel level, ChunkAccess chunk, Supplier<AttachmentType<T>> attachmentType, Supplier<P> payloadSupplier) {
		PacketDistributor.sendToPlayersTrackingChunk(level, chunk.getPos(), payloadSupplier.get());
		return Optional.ofNullable(chunk.removeData(attachmentType));
	}

	/**
	 * A version of {@link ChunkAccess#removeData(AttachmentType)} that additionally synchronizes the chunk data with clients. This method works with {@link AttachmentType}s with {@link Unit} data.
	 * @param level The level in which the chunk resides.
	 * @param chunk The chunk to set data in.
	 * @param attachmentType The {@link AttachmentType}.
	 */
	public static void removeChunkData(ServerLevel level, ChunkAccess chunk, DeferredHolder<AttachmentType<?>, AttachmentType<Unit>> attachmentType) {
		PacketDistributor.sendToPlayersTrackingChunk(
			level,
			chunk.getPos(),
			new UnitChunkAttachmentPayload(attachmentType.getKey(), chunk.getPos(), true)
		);
		chunk.removeData(attachmentType);
	}

	@SubscribeEvent
	public static void chunkSent(ChunkWatchEvent.Sent event) {
		if (event.getChunk().hasData(LegacyAttachments.LEGACY_CHUNK)) {
			PacketDistributor.sendToPlayer(
				event.getPlayer(),
				new LegacyChunkPayload(
					event.getPos(),
					Optional.of(event.getChunk().getData(LegacyAttachments.LEGACY_CHUNK))
				)
			);
		}

		if (event.getChunk().hasData(LegacyAttachments.ALLOW_ADVENTURE_MODE)) {
			PacketDistributor.sendToPlayer(
				event.getPlayer(),
				new UnitChunkAttachmentPayload(
					LegacyAttachments.ALLOW_ADVENTURE_MODE.getKey(),
					event.getPos(),
					false
				)
			);
		}
	}
}
