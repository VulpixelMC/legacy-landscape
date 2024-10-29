package gay.sylv.legacy_landscape.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.item.LegacyItems;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static gay.sylv.legacy_landscape.LegacyLandscape.MOD_ID;
import static gay.sylv.legacy_landscape.client.util.RenderUtil.VOID_COLOR;

public final class LegacyFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(
		BuiltInRegistries.FLUID,
		MOD_ID
	);

	public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> VOID_SOURCE = FLUIDS.register(
		"void",
		() -> new BaseFlowingFluid.Source(LegacyFluids.VOID_PROPERTIES)
	);

	public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> VOID_FLOWING = FLUIDS.register(
		"void_flowing",
		() -> new BaseFlowingFluid.Flowing(LegacyFluids.VOID_PROPERTIES)
	);

	public static final BaseFlowingFluid.Properties VOID_PROPERTIES = new BaseFlowingFluid.Properties(Types.VOID, VOID_SOURCE, VOID_FLOWING)
		.block(LegacyBlocks.VOID)
		.bucket(LegacyItems.VOID_BUCKET)
		.slopeFindDistance(3)
		.levelDecreasePerBlock(1);

	private LegacyFluids() {}

	public static final class Types {
		public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MOD_ID);

		public static final DeferredHolder<FluidType, FluidType> VOID = register(
			"void",
			FluidType.Properties.create()
				.lightLevel(0)
				.density(100_000)
				.viscosity(0)
				.sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK)
				.canDrown(false)
				.canSwim(false)
				.supportsBoating(false),
			VOID_COLOR,
			ResourceLocation.withDefaultNamespace("block/water_still"),
			ResourceLocation.withDefaultNamespace("block/water_flow"),
			ResourceLocation.withDefaultNamespace("block/water_overlay"),
			(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape) -> {
				if (mode == FogRenderer.FogMode.FOG_TERRAIN) {
					RenderSystem.setShaderFogStart(0.5F);
					RenderSystem.setShaderFogEnd(6.5F);
					RenderSystem.setShaderFogShape(FogShape.SPHERE);
					RenderSystem.setShaderFogColor(16.0F * -VOID_COLOR.getX() / 255.0F, 16.0F * -VOID_COLOR.getY() / 255.0F, 16.0F * -VOID_COLOR.getZ() / 255.0F);
				} else if (mode == FogRenderer.FogMode.FOG_SKY) {
					RenderSystem.setShaderFogColor(1.0F, 1.0F, 1.0F, 0.0F);
				}
			}
		);

		private static DeferredHolder<FluidType, FluidType> register(
			String name,
			FluidType.Properties properties,
			Vec3i color,
			ResourceLocation stillTexture,
			ResourceLocation flowingTexture,
			ResourceLocation overlayTexture,
			LegacyFluidType.Fog fog
		) {
			return register(
				name,
				properties,
				FastColor.ARGB32.color(color.getX(), color.getY(), color.getZ()),
				stillTexture,
				flowingTexture,
				overlayTexture,
				fog
			);
		}

		private static DeferredHolder<FluidType, FluidType> register(
			String name,
			FluidType.Properties properties,
			int color,
			ResourceLocation stillTexture,
			ResourceLocation flowingTexture,
			ResourceLocation overlayTexture,
			LegacyFluidType.Fog fog
		) {
			return FLUID_TYPES.register(
				name,
				() -> new LegacyFluidType(
					properties,
					color,
					stillTexture,
					flowingTexture,
					overlayTexture,
					fog
				)
			);
		}
	}

	@EventBusSubscriber(
		modid = MOD_ID,
		value = Dist.CLIENT,
		bus = EventBusSubscriber.Bus.MOD
	)
	public static final class ClientEvents {
		private ClientEvents() {}

		@SubscribeEvent
		public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
			for (FluidType fluidType : NeoForgeRegistries.FLUID_TYPES) {
				if (fluidType instanceof LegacyFluidType legacyFluidType) {
					event.registerFluidType(
						new IClientFluidTypeExtensions() {
							@Override
							public int getTintColor() {
								return legacyFluidType.getColor();
							}

							@Override
							public @NotNull ResourceLocation getStillTexture() {
								return legacyFluidType.getStillTexture();
							}

							@Override
							public @NotNull ResourceLocation getFlowingTexture() {
								return legacyFluidType.getFlowingTexture();
							}

							@Override
							public @Nullable ResourceLocation getOverlayTexture() {
								return legacyFluidType.getOverlayTexture();
							}

							@Override
							public void modifyFogRender(
								@NotNull Camera camera,
								FogRenderer.@NotNull FogMode mode,
								float renderDistance,
								float partialTick,
								float nearDistance,
								float farDistance,
								@NotNull FogShape shape
							) {
								legacyFluidType.getFog().render(
									camera,
									mode,
									renderDistance,
									partialTick,
									nearDistance,
									farDistance,
									shape
								);
							}
						},
						legacyFluidType
					);
				}
			}
		}
	}
}
