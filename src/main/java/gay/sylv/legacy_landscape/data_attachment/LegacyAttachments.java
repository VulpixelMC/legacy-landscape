package gay.sylv.legacy_landscape.data_attachment;

import com.mojang.serialization.Codec;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.GAME
)
public final class LegacyAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

	public static final Supplier<AttachmentType<Boolean>> LEGACY_CHUNK = ATTACHMENT_TYPES.register(
		"legacy_chunk",
		() -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
	);

	private LegacyAttachments() {}

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
		return Optional.ofNullable(chunk.setData(attachmentType.get(), data));
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
		return Optional.ofNullable(chunk.removeData(attachmentType.get()));
	}

	@SubscribeEvent
	public static void chunkSent(ChunkWatchEvent.Sent event) {
		if (event.getChunk().hasData(LegacyAttachments.LEGACY_CHUNK)) {
			PacketDistributor.sendToPlayer(event.getPlayer(), new LegacyChunkPayload(event.getPos(), event.getChunk().getData(LegacyAttachments.LEGACY_CHUNK)));
		}
	}
}
