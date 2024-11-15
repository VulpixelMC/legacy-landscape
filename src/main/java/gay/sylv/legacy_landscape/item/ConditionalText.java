package gay.sylv.legacy_landscape.item;

import gay.sylv.legacy_landscape.api.definitions.data_components.Broken;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface ConditionalText {
	@NotNull Predicate<TooltipFlag> legacy_landscape$getCondition();
	void legacy_landscape$setCondition(TooltipCondition condition);
	@NotNull Broken legacy_landscape$getBroken();
	void legacy_landscape$setBroken(Broken broken);
}
