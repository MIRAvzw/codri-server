/**
 * Copyright (C) 2012 Tim Besard <tim.besard@gmail.com>
 *
 * All rights reserved.
 */

package be.mira.codri.spring;

/**
 * Indicates Logger of appropriate type to be supplied at runtime to the
 * annotated field.
 *
 * The injected logger is an appropriate implementation of org.slf4j.Logger.
 */
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface Slf4jLogger {
}
