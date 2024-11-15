package gay.sylv.legacy_landscape.mixin;

import gay.sylv.legacy_landscape.api.definitions.data_components.Broken;
import gay.sylv.legacy_landscape.item.ConditionalText;
import gay.sylv.legacy_landscape.item.TooltipCondition;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;
import java.util.function.Predicate;

@Mixin(MutableComponent.class)
public final class Mixin_MutableComponent implements ConditionalText {
	@Unique
	private static final TooltipCondition legacy_landscape$ALWAYS = new TooltipCondition(flag -> true);

	@Unique
	private TooltipCondition legacy_landscape$condition;

	@Unique
	private Broken legacy_landscape$broken;

	@Override
	public @NotNull Predicate<TooltipFlag> legacy_landscape$getCondition() {
		return Objects.requireNonNullElse(
			legacy_landscape$condition,
			legacy_landscape$ALWAYS
		).condition();
	}

	@Override
	public void legacy_landscape$setCondition(TooltipCondition condition) {
		legacy_landscape$condition = condition;
	}

	@Override
	public @NotNull Broken legacy_landscape$getBroken() {
		return Objects.requireNonNullElse(legacy_landscape$broken, Broken.unbroken());
	}

	@Override
	public void legacy_landscape$setBroken(Broken broken) {
		legacy_landscape$broken = broken;
	}
}
