package gay.sylv.legacy_landscape.mixin;

import gay.sylv.legacy_landscape.util.PackUtil;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = BuiltInPackSource.class, priority = 999)
public abstract class Mixin_BuiltInPackSource {
	@Inject(
		method = "listBundledPacks",
		at = @At("RETURN")
	)
	private void addBuiltinResourcePacks(Consumer<Pack> packConsumer, CallbackInfo ci) {
		packConsumer.accept(PackUtil.legacy_landscape$createBuiltinPack(PackType.CLIENT_RESOURCES));
		packConsumer.accept(PackUtil.legacy_landscape$createBuiltinPack(PackType.SERVER_DATA));
	}
}
