package gay.sylv.legacy_landscape.mixin.client;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderType.CompositeRenderType.class)
public interface Accessor_CompositeRenderType {
	@Accessor
	RenderType.CompositeState getState();
}
