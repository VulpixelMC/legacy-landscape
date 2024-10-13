package gay.sylv.legacy_landscape.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderSystem.class)
public interface Accessor_RenderSystem {
	@Accessor
	static int[] getShaderTextures() {
		throw new UnsupportedOperationException();
	}
}
