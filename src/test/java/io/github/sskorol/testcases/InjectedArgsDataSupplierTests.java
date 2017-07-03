package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.model.User;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InjectedArgsDataSupplierTests {

    @DataSupplier(extractValues = true)
    public List<String> getFullMetaData(final ITestContext context, final Method method) {
        return asList(context.getCurrentXmlTest().getName(), method.getName());
    }

    @DataSupplier
    public String getContextMetaData(final ITestContext context) {
        return context.getCurrentXmlTest().getName();
    }

    @DataSupplier
    public String getMethodMetaData(final Method method) {
        return method.getName();
    }

    @Test(dataProvider = "getFullMetaData")
    public void supplyFullMetaData(final String contextName, final String methodName) {
        assertThat(contextName).isEqualTo("DataSupplier tests");
        assertThat(methodName).isEqualTo("supplyFullMetaData");
    }

    @Test(dataProvider = "getContextMetaData")
    public void supplyContextMetaData(final String contextName) {
        assertThat(contextName).isEqualTo("DataSupplier tests");
    }

    @Test(dataProvider = "getMethodMetaData")
    public void supplyMethodMetaData(final String methodName) {
        assertThat(methodName).isEqualTo("supplyMethodMetaData");
    }
}
