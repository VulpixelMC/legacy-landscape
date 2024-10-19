package gay.sylv.legacy_landscape.item;

import net.minecraft.world.item.TooltipFlag;

import java.util.function.Predicate;

public record TooltipCondition(Predicate<TooltipFlag> condition) {
	public static boolean hasModifierKey(TooltipFlag flag) {
		return flag.hasShiftDown() || flag.hasControlDown() || flag.hasAltDown();
	}

	public static Predicate<TooltipFlag> not(Predicate<TooltipFlag> condition) {
		return condition.negate();
	}
}
