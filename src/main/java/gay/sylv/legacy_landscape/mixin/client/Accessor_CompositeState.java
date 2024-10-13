package gay.sylv.legacy_landscape.mixin.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderType.CompositeState.class)
public interface Accessor_CompositeState {
	@Accessor
	RenderStateShard.EmptyTextureStateShard getTextureState();
}
