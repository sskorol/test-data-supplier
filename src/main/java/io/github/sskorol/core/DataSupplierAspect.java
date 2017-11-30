package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.util.List;

import static io.github.sskorol.utils.ServiceLoaderUtils.load;

/**
 * Key aspect for DataSupplier interception.
 */
@Aspect
public class DataSupplierAspect {

    private static final List<DataSupplierInterceptor> DATA_SUPPLIERS =
            load(DataSupplierInterceptor.class, DataSupplierAspect.class.getClassLoader());

    @Before("execution(@org.testng.annotations.DataProvider * io.github.sskorol.core.DataProviderTransformer.*(..))")
    public void beforeDataProviderCall(final JoinPoint joinPoint) {
        DATA_SUPPLIERS.forEach(ds -> ds.beforeDataPreparation((ITestContext) joinPoint.getArgs()[0],
                (ITestNGMethod) joinPoint.getArgs()[1]));
    }

    @After("execution(@org.testng.annotations.DataProvider * io.github.sskorol.core.DataProviderTransformer.*(..))")
    public void afterDataProviderCall(final JoinPoint joinPoint) {
        DATA_SUPPLIERS.forEach(ds -> ds.afterDataPreparation((ITestContext) joinPoint.getArgs()[0],
                (ITestNGMethod) joinPoint.getArgs()[1]));
    }

    @Around("execution(* io.github.sskorol.core.DataProviderTransformer.getMetaData(..))")
    public DataSupplierMetaData onDataPreparation(final ProceedingJoinPoint joinPoint) throws Throwable {
        final DataSupplierMetaData metaData = (DataSupplierMetaData) joinPoint.proceed(joinPoint.getArgs());
        DATA_SUPPLIERS.forEach(ds -> ds.onDataPreparation(metaData));
        return metaData;
    }

    public static List<DataSupplierInterceptor> getInterceptors() {
        return DATA_SUPPLIERS;
    }
}
