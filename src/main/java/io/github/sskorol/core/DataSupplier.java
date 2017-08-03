package io.github.sskorol.core;

import java.lang.annotation.*;

/**
 * Key annotation which replaces common DataProvider.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSupplier {

    String name() default "";

    boolean extractValues() default false;

    boolean runInParallel() default false;
}
