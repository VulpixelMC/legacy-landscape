package gay.sylv.legacy_landscape.api;

import com.mojang.blaze3d.platform.NativeImage;
import gay.sylv.legacy_landscape.datagen.runtime.RuntimeResourcePackImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Map;

@SuppressWarnings("deprecation")
public interface RuntimeResourcePack extends PackResources {
	static RuntimeResourcePack getInstance() {
		return RuntimeResourcePackImpl.INSTANCE;
	}

	static Pack.ResourcesSupplier fixedResources() {
		return RuntimeResourcePackImpl.FIXED_RESOURCES;
	}

	static PackSelectionConfig builtInSelectionConfig() {
		return RuntimeResourcePackImpl.BUILT_IN_SELECTION_CONFIG;
	}

	Map<ResourceLocation, String> getItemMcmeta();

	void addItemMcmeta(ResourceLocation id, String mcmetaJson);

	Map<ResourceLocation, String> getBlockMcmeta();

	void addBlockMcmeta(ResourceLocation id, String mcmetaJson);

	Map<ResourceLocation, String> getItemTags();

	void addItemTag(ResourceLocation id, String tagJson);

	Map<ResourceLocation, String> getModels();

	void addModel(ResourceLocation id, String modelJson);

	Map<ResourceLocation, NativeImage> getItemTextures();

	void addItemTexture(ResourceLocation id, NativeImage image);

	Map<ResourceLocation, NativeImage> getBlockTextures();

	void addBlockTexture(ResourceLocation id, NativeImage image);

	@Nullable
	IoSupplier<InputStream> getResource(ResourceLocation id);
}
