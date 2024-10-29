package gay.sylv.legacy_landscape.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class LegacyFluidType extends FluidType {
	private final int color;
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final ResourceLocation overlayTexture;
	private final Fog fog;
	private final FogColor fogColor;

	public LegacyFluidType(
		Properties properties,
		int color,
		ResourceLocation stillTexture,
		ResourceLocation flowingTexture,
		ResourceLocation overlayTexture,
		Fog fog,
		FogColor fogColor
	) {
		super(properties);

		this.color = color;
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.fog = fog;
		this.fogColor = fogColor;
	}

	public int getColor() {
		return color;
	}

	public ResourceLocation getStillTexture() {
		return stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return flowingTexture;
	}

	public ResourceLocation getOverlayTexture() {
		return overlayTexture;
	}

	public Fog getFog() {
		return fog;
	}

	public FogColor getFogColor() {
		return fogColor;
	}

	@FunctionalInterface
	public interface Fog {
		Fog NONE = (camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape) -> {};

		void render(
			@NotNull Camera camera,
			FogRenderer.@NotNull FogMode mode,
			float renderDistance,
			float partialTick,
			float nearDistance,
			float farDistance,
			@NotNull FogShape shape
		);
	}

	@FunctionalInterface
	public interface FogColor {
		FogColor NONE = (camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor) -> fluidFogColor;

		@NotNull Vector3f modify(
			@NotNull Camera camera,
			float partialTick,
			@NotNull ClientLevel level,
			int renderDistance,
			float darkenWorldAmount,
			@NotNull Vector3f fluidFogColor
		);
	}
}
