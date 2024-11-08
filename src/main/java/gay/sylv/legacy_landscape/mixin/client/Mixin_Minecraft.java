package gay.sylv.legacy_landscape.mixin.client;

import gay.sylv.legacy_landscape.client.LegacyResourceFaker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public abstract class Mixin_Minecraft {
	@Shadow
	public abstract CompletableFuture<Void> reloadResourcePacks();

	@Inject(
		method = "onResourceLoadFinished",
		at = @At("TAIL")
	)
	private void onFinishLoad(Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
		LegacyResourceFaker.loadLegacyResources();
	}

	@Inject(
		method = "onGameLoadFinished",
		at = @At("TAIL")
	)
	private void onGameLoadFinished(Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
		this.reloadResourcePacks();
	}
}
