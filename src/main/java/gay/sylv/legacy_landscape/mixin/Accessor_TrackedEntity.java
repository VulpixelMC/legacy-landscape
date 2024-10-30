package gay.sylv.legacy_landscape.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.network.ServerPlayerConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ChunkMap.TrackedEntity.class)
public interface Accessor_TrackedEntity {
	@Accessor
	Set<ServerPlayerConnection> getSeenBy();
	@Accessor
	ServerEntity getServerEntity();
}
