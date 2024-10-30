package gay.sylv.legacy_landscape.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkMap.class)
public interface Accessor_ChunkMap {
	@Accessor
	Int2ObjectMap<ChunkMap.TrackedEntity> getEntityMap();
}
