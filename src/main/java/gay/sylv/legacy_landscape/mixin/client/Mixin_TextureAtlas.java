package gay.sylv.legacy_landscape.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Patch TextureAtlas to allow us to load legacy textures or default to regular textures.
 */
@Mixin(TextureAtlas.class)
public final class Mixin_TextureAtlas {
	@Shadow
	@Nullable
	private TextureAtlasSprite missingSprite;

	private Mixin_TextureAtlas() {}

	@WrapOperation(
		method = "getSprite",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
		)
	)
	private Object hookLegacySprite(Map<ResourceLocation, TextureAtlasSprite> instance, Object key, Object defaultValue, Operation<Object> original) {
		if (((ResourceLocation) key).getPath().startsWith("block/legacy/")) {
			ResourceLocation defaultKey = ((ResourceLocation) key).withPath(((ResourceLocation) key).getPath().replace("legacy/", ""));
			return original.call(instance, key, instance.getOrDefault(defaultKey, this.missingSprite));
		} else {
			return original.call(instance, key, defaultValue);
		}
	}
}
