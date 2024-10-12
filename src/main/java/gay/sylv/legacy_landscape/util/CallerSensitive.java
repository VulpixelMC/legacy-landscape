package gay.sylv.legacy_landscape.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that a method is caller-sensitive. This means that you <b>MUST not</b> wrap the annotated method.
 */
@Target({ElementType.METHOD})
public @interface CallerSensitive {
}
