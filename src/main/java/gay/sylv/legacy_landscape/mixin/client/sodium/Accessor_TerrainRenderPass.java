package gay.sylv.legacy_landscape.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TerrainRenderPass.class)
@Pseudo
public interface Accessor_TerrainRenderPass {
	@Accessor
	RenderType getRenderType();
}
