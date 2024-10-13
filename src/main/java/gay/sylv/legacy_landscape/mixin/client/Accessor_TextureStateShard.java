package gay.sylv.legacy_landscape.mixin.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(RenderStateShard.TextureStateShard.class)
public interface Accessor_TextureStateShard {
	@Accessor
	Optional<ResourceLocation> getTexture();

	@Accessor
	boolean isBlur();

	@Accessor
	boolean isMipmap();
}
