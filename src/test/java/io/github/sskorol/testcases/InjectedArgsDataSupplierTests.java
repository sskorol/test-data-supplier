package io.github.sskorol.testcases;

import io.github.sskorol.core.DataSupplier;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InjectedArgsDataSupplierTests {

    @DataSupplier(transpose = true)
    public List<String> getFullMetaData(final ITestContext context, final Method method, final ITestNGMethod testNGMethod) {
        return asList(context.getCurrentXmlTest().getName(), method.getName(), testNGMethod.getDescription());
    }

    @DataSupplier
    public String getContextMetaData(final ITestContext context) {
        return context.getCurrentXmlTest().getName();
    }

    @DataSupplier
    public String getMethodMetaData(final Method method) {
        return method.getName();
    }

    @DataSupplier
    public String getITestNGMethodMetaData(final ITestNGMethod method) {
        return method.getConstructorOrMethod().getName();
    }

    @DataSupplier
    public String getWrongArgTypeMetaData(final String ob) {
        return "data";
    }

    @DataSupplier
    public String getNullArgTypeMetaData(final String ob) {
        return ob;
    }

    @Test(dataProvider = "getFullMetaData", description = "test description")
    public void supplyFullMetaData(final String contextName, final String methodName, final String description) {
        assertThat(contextName).isEqualTo("DataSupplier tests");
        assertThat(methodName).isEqualTo("supplyFullMetaData");
        assertThat(description).isEqualTo("test description");
    }

    @Test(dataProvider = "getContextMetaData")
    public void supplyContextMetaData(final String contextName) {
        assertThat(contextName).isEqualTo("DataSupplier tests");
    }

    @Test(dataProvider = "getMethodMetaData")
    public void supplyMethodMetaData(final String methodName) {
        assertThat(methodName).isEqualTo("supplyMethodMetaData");
    }

    @Test(dataProvider = "getITestNGMethodMetaData")
    public void supplyITestNGMethodMetaData(final String methodName) {
        assertThat(methodName).isEqualTo("supplyITestNGMethodMetaData");
    }

    @Test(dataProvider = "getWrongArgTypeMetaData")
    public void supplyWrongArgTypeMethodMetaData(final String ob) {
        assertThat(ob).isEqualTo("data");
    }

    @Test(dataProvider = "getNullArgTypeMetaData")
    public void supplyNullArgTypeMethodMetaData(final String ob) {
        // body won't be ever reached
    }
}
