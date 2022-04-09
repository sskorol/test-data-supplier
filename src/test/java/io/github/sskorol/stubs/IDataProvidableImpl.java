package io.github.sskorol.stubs;

import org.testng.internal.annotations.IDataProvidable;

public class IDataProvidableImpl<T> implements IDataProvidable {

    private final Class<T> targetClass;

    public IDataProvidableImpl(final Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public String getDataProvider() {
        return null;
    }

    @Override
    public void setDataProvider(final String dataProvider) {
    }

    @Override
    public Class<?> getDataProviderClass() {
        return targetClass;
    }

    @Override
    public void setDataProviderClass(final Class<?> dataProviderClass) {
    }

    @Override
    public String getDataProviderDynamicClass() {
        return null;
    }

    @Override
    public void setDataProviderDynamicClass(final String dataProviderDynamicClass) {
    }
}
