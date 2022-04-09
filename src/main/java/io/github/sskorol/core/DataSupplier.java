package io.github.sskorol.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Key annotation which replaces common DataProvider.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSupplier {

    String name() default "";

    boolean transpose() default false;

    boolean flatMap() default false;

    boolean runInParallel() default false;

    int[] indices() default {};

    boolean propagateTestFailure() default false;
}
