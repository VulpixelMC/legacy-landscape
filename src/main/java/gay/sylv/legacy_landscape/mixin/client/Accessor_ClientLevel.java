package gay.sylv.legacy_landscape.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.ColorResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface Accessor_ClientLevel {
	@Accessor
	Object2ObjectArrayMap<ColorResolver, BlockTintCache> getTintCaches();
}
