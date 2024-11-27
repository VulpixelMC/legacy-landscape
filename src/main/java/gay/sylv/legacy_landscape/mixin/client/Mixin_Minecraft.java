package gay.sylv.legacy_landscape.mixin.client;

import gay.sylv.legacy_landscape.client.LegacyResourceFaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Musics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public abstract class Mixin_Minecraft {
	@Shadow
	public abstract CompletableFuture<Void> reloadResourcePacks();

	@Shadow
	public abstract MusicManager getMusicManager();

	@Unique
	private boolean legacy_landscape$secondGameLoadFinished = false;

	@Inject(
		method = "onResourceLoadFinished",
		at = @At("HEAD")
	)
	private void onFinishLoad(Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
		this.getMusicManager().stopPlaying();
		LegacyResourceFaker.loadLegacyResources();
		if (legacy_landscape$secondGameLoadFinished) {
			legacy_landscape$secondGameLoadFinished = false;
			this.getMusicManager().startPlaying(Musics.MENU);
		}
	}

	@Inject(
		method = "onGameLoadFinished",
		at = @At("TAIL")
	)
	private void onGameLoadFinished(Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
		legacy_landscape$secondGameLoadFinished = true;
		this.reloadResourcePacks();
	}
}
