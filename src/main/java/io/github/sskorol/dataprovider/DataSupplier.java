package io.github.sskorol.dataprovider;

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

    boolean extractValues() default false;

    boolean runInParallel() default false;
}
