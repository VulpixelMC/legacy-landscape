package gay.sylv.legacy_landscape.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public final class LegacyFluidType extends FluidType {
	private final int color;
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final ResourceLocation overlayTexture;
	private final Fog fog;

	public LegacyFluidType(
		Properties properties,
		int color,
		ResourceLocation stillTexture,
		ResourceLocation flowingTexture,
		ResourceLocation overlayTexture,
		Fog fog
	) {
		super(properties);

		this.color = color;
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.fog = fog;
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
}
