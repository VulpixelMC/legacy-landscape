package gay.sylv.legacy_landscape.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import gay.sylv.legacy_landscape.CommonConfig;
import gay.sylv.legacy_landscape.api.definitions.effect.MobEffects;
import gay.sylv.legacy_landscape.block.LegacyBlocks;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.api.definitions.data_components.Broken;
import gay.sylv.legacy_landscape.data_components.LegacyComponents;
import gay.sylv.legacy_landscape.effect.LegacyEffects;
import gay.sylv.legacy_landscape.item.LegacyItems;
import gay.sylv.legacy_landscape.util.Constants;
import gay.sylv.legacy_landscape.util.Maths;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID
)
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

	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		// Apply Evanescence to entities in void fluid.
		if (entity instanceof LivingEntity livingEntity && livingEntity.isInFluidType(Types.VOID.get())) {
			LegacyEffects.apply(
				livingEntity,
				MobEffects.evanescence(),
				Maths.tickMinute(10)
			);
		}
	}

	@SubscribeEvent
	public static void transmuteWater(EntityTickEvent.Post event) {
		// If we are in demo mode and disallowed from transmuting water, cancel callback.
		ChunkPos chunkPos = event.getEntity().chunkPosition();
		if (
			CommonConfig.isDemoMode() &&
			!event.getEntity()
				.level()
				.getChunk(chunkPos.x, chunkPos.z)
				.hasData(LegacyAttachments.ALLOW_ADVENTURE_MODE)
		) {
			return;
		}

		if (
			event.getEntity() instanceof ItemEntity entity &&
			entity.isInWater() &&
			entity.getItem().is(LegacyItems.JAPPAS_WAND) &&
			Objects.requireNonNullElse(entity.getItem().get(LegacyComponents.BROKEN), Broken.unbroken()).equals(Broken.of(1)) &&
			!entity.level().isClientSide()
		) {
			ServerLevel level = (ServerLevel) entity.level();
			entity.kill();
			level.setBlock(entity.blockPosition(), LegacyFluids.VOID_SOURCE.get().defaultFluidState().createLegacyBlock(), 11);
		}
	}

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
			Constants.VOID_COLOR,
			ResourceLocation.withDefaultNamespace("block/water_still"),
			ResourceLocation.withDefaultNamespace("block/water_flow"),
			ResourceLocation.withDefaultNamespace("block/water_overlay")
		);

		private static DeferredHolder<FluidType, FluidType> register(
			String name,
			FluidType.Properties properties,
			Vec3i color,
			ResourceLocation stillTexture,
			ResourceLocation flowingTexture,
			ResourceLocation overlayTexture
		) {
			return register(
				name,
				properties,
				FastColor.ARGB32.color(color.getX(), color.getY(), color.getZ()),
				stillTexture,
				flowingTexture,
				overlayTexture
			);
		}

		private static DeferredHolder<FluidType, FluidType> register(
			String name,
			FluidType.Properties properties,
			int color,
			ResourceLocation stillTexture,
			ResourceLocation flowingTexture,
			ResourceLocation overlayTexture
		) {
			return FLUID_TYPES.register(
				name,
				() -> new LegacyFluidType(
					properties,
					color,
					stillTexture,
					flowingTexture,
					overlayTexture
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
			LegacyFluidType voidType = (LegacyFluidType) Types.VOID.get();
			voidType.setFogProperties(
				(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape) -> {
					RenderSystem.setShaderFogStart(0.5F);
					RenderSystem.setShaderFogEnd(6.5F);
					RenderSystem.setShaderFogShape(FogShape.SPHERE);
				},
				(camera, partialTick, level, renderDistance, darkenWorldAmount, fluidFogColor) -> Constants.VOID_FOG_COLOR
			);

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

							@Override
							public @NotNull Vector3f modifyFogColor(
								@NotNull Camera camera,
								float partialTick,
								@NotNull ClientLevel level,
								int renderDistance,
								float darkenWorldAmount,
								@NotNull Vector3f fluidFogColor
							) {
								return legacyFluidType.getFogColor().modify(
									camera,
									partialTick,
									level,
									renderDistance,
									darkenWorldAmount,
									fluidFogColor
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
