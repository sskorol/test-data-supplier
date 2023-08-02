package io.github.sskorol.data;

import java.lang.annotation.*;

/**
 * Use this annotation to provide Excel sheet name. If none is specified hte first one is used.
 */
@Repeatable(Sheets.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {

    String name();
}
