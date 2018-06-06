package io.github.sskorol.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to provide CSV / JSON source path. Note that local path should be relative to resources folder.
 * As an alternative you can provide a URL.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Source {

    String path();
}
