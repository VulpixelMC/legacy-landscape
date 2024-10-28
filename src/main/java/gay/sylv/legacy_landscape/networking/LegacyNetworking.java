package gay.sylv.legacy_landscape.networking;

import com.mojang.logging.LogUtils;
import gay.sylv.legacy_landscape.networking.client_bound.LegacyChunkPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.MOD
)
public final class LegacyNetworking {
	public static final Logger LOGGER = LogUtils.getLogger();

	private LegacyNetworking() {}

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		final PayloadRegistrar mainThreadRegistrar = registrar.executesOn(HandlerThread.MAIN);
		mainThreadRegistrar.playToClient(
			LegacyChunkPayload.TYPE,
			LegacyChunkPayload.STREAM_CODEC,
			ClientPayloadHandler::handleLegacyChunkPayload
		);
	}
}
