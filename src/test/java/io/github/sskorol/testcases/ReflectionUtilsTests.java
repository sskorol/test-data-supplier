package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.ReflectionUtils;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import org.testng.annotations.Test;

import static io.github.sskorol.dataprovider.ReflectionUtils.getMethod;
import static org.assertj.core.api.Assertions.*;
import static org.joor.Reflect.on;

public class ReflectionUtilsTests {

    @Test
    public void shouldThrowAnExceptionOnConstructorAccess() {
        assertThatThrownBy(() -> on(ReflectionUtils.class).create())
                .hasStackTraceContaining("java.lang.UnsupportedOperationException: Illegal access to private constructor");
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void shouldThrowAnExceptionOnNonExistingMethodAccess() {
        getMethod(ExternalDataSuppliers.class, "missingMethodName");
    }
}
