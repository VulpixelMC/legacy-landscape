package gay.sylv.legacy_landscape.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import gay.sylv.legacy_landscape.api.RuntimeResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class LegacyResourceFaker {
	private static final Logger LOGGER = LogUtils.getLogger();

	private LegacyResourceFaker() {}

	public static void loadLegacyResources() {
		Minecraft client = Minecraft.getInstance();

		try (PackResources programmerArtResources = Objects.requireNonNull(client.getResourcePackRepository().getPack("programmer_art")).open()) {
			Set<String> namespaces = programmerArtResources.getNamespaces(PackType.CLIENT_RESOURCES);
			Map<ResourceLocation, IoSupplier<InputStream>> textures = new HashMap<>();
			namespaces.forEach(namespace -> programmerArtResources.listResources(PackType.CLIENT_RESOURCES, namespace, "textures/block", textures::put));
			RuntimeResourcePack rrp = RuntimeResourcePack.getInstance();
			rrp.getBlockMcmeta().clear();
			rrp.getBlockTextures().clear();
			textures.forEach((key, value) -> {
				try {
					ResourceLocation newKey = key.withPath(path -> path.replace("textures/block/", "legacy/").replace(".png", "").replace(".mcmeta", ""));
					if (!key.getPath().endsWith(".mcmeta")) {
						// Scary!
						// SAFETY: Once the RuntimeResourcePack is closed, the NativeImage is closed.
						NativeImage nativeImage = NativeImage.read(value.get());
						rrp.addBlockTexture(newKey, nativeImage);
					} else {
						rrp.addBlockMcmeta(newKey, new String(value.get().readAllBytes(), StandardCharsets.UTF_8));
					}
				} catch (IOException e) {
					LOGGER.error("Failed to load texture {}", key, e);
				}
			});
		}
	}
}
