package gay.sylv.legacy_landscape.item;

import gay.sylv.legacy_landscape.api.definitions.data_components.Broken;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TooltipItem extends Item {
	private final List<Component> tooltip;

	public TooltipItem(Properties properties) {
		super(properties);
		this.tooltip = properties.tooltip;
	}

	/**
	 * Controls if the tooltip should be shown based on the given context.
	 * @param stack The {@link ItemStack} to show the tooltip on.
	 * @param context The {@link Item.TooltipContext}.
	 * @param tooltip The {@link Component} of the tooltip.
	 * @param tooltipFlag The {@link TooltipFlag}s of the tooltip.
	 * @return whether the tooltip should be shown.
	 */
	@SuppressWarnings("unused")
	protected boolean showTooltip(
		@NotNull ItemStack stack,
		@NotNull TooltipContext context,
		@NotNull Component tooltip,
		@NotNull TooltipFlag tooltipFlag
	) {
		return true;
	}

	@Override
	public void appendHoverText(
		@NotNull ItemStack stack,
		@NotNull TooltipContext context,
		@NotNull List<Component> tooltipComponents,
		@NotNull TooltipFlag tooltipFlag
	) {
		tooltipComponents.addAll(
			tooltip
				.stream()
				.filter(
					tooltip -> {
						boolean condition = ((ConditionalText) tooltip)
							.legacy_landscape$getCondition()
							.test(tooltipFlag);
						boolean showTooltip = showTooltip(stack, context, tooltip, tooltipFlag);
						return condition && showTooltip;
					}
				)
				.toList()
		);
	}

	public static final class Properties extends Item.Properties {
		List<Component> tooltip = new ArrayList<>();

		public Properties tooltip(Component tooltip) {
			this.tooltip.add(tooltip);
			return this;
		}

		public Properties tooltip(Predicate<TooltipFlag> condition, Component tooltip) {
			((ConditionalText) tooltip).legacy_landscape$setCondition(new TooltipCondition(condition));
			this.tooltip.add(tooltip);
			return this;
		}

		public Properties tooltip(Predicate<TooltipFlag> condition, Broken broken, Component tooltip) {
			((ConditionalText) tooltip).legacy_landscape$setCondition(new TooltipCondition(condition));
			((ConditionalText) tooltip).legacy_landscape$setBroken(broken);
			this.tooltip.add(tooltip);
			return this;
		}

		public Properties moreInfo() {
			return this
				.tooltip(
					TooltipCondition.not(TooltipCondition::hasModifierKey),
					Broken.always(),
					Component.translatable("tooltip.legacy_landscape.more_info")
						.withStyle(
							ChatFormatting.ITALIC,
							ChatFormatting.GRAY
						)
				);
		}

		public @NotNull Properties food(@NotNull FoodProperties food) {
			return (Properties) super.food(food);
		}

		public @NotNull Properties stacksTo(int maxStackSize) {
			return (Properties) super.stacksTo(maxStackSize);
		}

		public @NotNull Properties durability(int maxDamage) {
			return (Properties) super.durability(maxDamage);
		}

		public @NotNull Properties craftRemainder(@NotNull Item craftingRemainingItem) {
			return (Properties) super.craftRemainder(craftingRemainingItem);
		}

		public @NotNull Properties rarity(@NotNull Rarity rarity) {
			return (Properties) super.rarity(rarity);
		}

		public @NotNull Properties fireResistant() {
			return (Properties) super.fireResistant();
		}

		public @NotNull Properties jukeboxPlayable(@NotNull ResourceKey<JukeboxSong> song) {
			return (Properties) super.jukeboxPlayable(song);
		}

		public @NotNull Properties setNoRepair() {
			return (Properties) super.setNoRepair();
		}

		public @NotNull Properties requiredFeatures(FeatureFlag @NotNull ... requiredFeatures) {
			return (Properties) super.requiredFeatures(requiredFeatures);
		}

		public <T> @NotNull Properties component(@NotNull DataComponentType<T> component, @NotNull T value) {
			return (Properties) super.component(component, value);
		}

		public @NotNull Properties attributes(@NotNull ItemAttributeModifiers attributes) {
			return (Properties) super.attributes(attributes);
		}
	}
}
