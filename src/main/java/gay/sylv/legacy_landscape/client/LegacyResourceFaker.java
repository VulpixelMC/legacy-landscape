package gay.sylv.legacy_landscape.client;

import com.mojang.logging.LogUtils;
import gay.sylv.legacy_landscape.LegacyLandscape;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Unit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(
	modid = LegacyLandscape.MOD_ID,
	bus = EventBusSubscriber.Bus.MOD
)
public final class LegacyResourceFaker {
	private static final Logger LOGGER = LogUtils.getLogger();

	private static boolean initialized;
	private static PackResources programmerArt;
	private static ReloadableResourceManager resourceManager;
	private static TextureManager textureManager;
	private static SplashManager splashManager;
	private static BlockColors blockColors;
	private static ModelManager modelManager;
	private static EntityModelSet entityModels;

	@SubscribeEvent
	private static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((ResourceManagerReloadListener) resourceManager1 -> {
			Minecraft client = Minecraft.getInstance();
			if (!initialized) {
				try (PackResources programmerArtResources = Objects.requireNonNull(client.getResourcePackRepository().getPack("programmer_art")).open()) {
					programmerArt = programmerArtResources;
					resourceManager = new ReloadableResourceManager(PackType.CLIENT_RESOURCES);
					textureManager = new TextureManager(resourceManager);
					resourceManager.registerReloadListener(textureManager);
					splashManager = new SplashManager(Minecraft.getInstance().getUser());
					resourceManager.registerReloadListener(splashManager);
					resourceManager.registerReloadListener(new GrassColorReloadListener());
					resourceManager.registerReloadListener(new FoliageColorReloadListener());
					blockColors = BlockColors.createDefault();
					modelManager = new ModelManager(textureManager, blockColors, client.options.mipmapLevels().get());
					resourceManager.registerReloadListener(modelManager);
					entityModels = new EntityModelSet();
					resourceManager.registerReloadListener(entityModels);
				}

				initialized = true;
			}

			modelManager.updateMaxMipLevel(client.options.mipmapLevels().get());

			List<PackResources> fullResourcesList = client.getResourceManager().listPacks().collect(Collectors.toCollection(ArrayList::new));
			fullResourcesList.add(programmerArt);
			resourceManager.createReload(Util.backgroundExecutor(), client, CompletableFuture.completedFuture(Unit.INSTANCE), fullResourcesList).done().thenRun(() -> LOGGER.info("Loaded programmer art resources"));
		});
	}

	public static TextureManager getTextureManager() {
		return textureManager;
	}
}
