package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.testng.ITestContext;

import java.lang.reflect.Method;
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
        DATA_SUPPLIERS.forEach(dataSupplier -> dataSupplier.beforeDataPreparation((ITestContext) joinPoint.getArgs()[0],
                (Method) joinPoint.getArgs()[1]));
    }

    @After("execution(@org.testng.annotations.DataProvider * io.github.sskorol.core.DataProviderTransformer.*(..))")
    public void afterDataProviderCall(final JoinPoint joinPoint) {
        DATA_SUPPLIERS.forEach(dataSupplier -> dataSupplier.afterDataPreparation((ITestContext) joinPoint.getArgs()[0],
                (Method) joinPoint.getArgs()[1]));
    }

    @SuppressWarnings("FinalLocalVariable")
    @Around("execution(* io.github.sskorol.core.DataProviderTransformer.getDataSupplierMetaData(..))")
    public DataSupplierMetaData onDataPreparation(final ProceedingJoinPoint joinPoint) throws Throwable {
        val dataSupplierMetaData = (DataSupplierMetaData) joinPoint.proceed(joinPoint.getArgs());
        DATA_SUPPLIERS.forEach(ds -> ds.onDataPreparation(dataSupplierMetaData));
        return dataSupplierMetaData;
    }

    public static List<DataSupplierInterceptor> getInterceptors() {
        return DATA_SUPPLIERS;
    }
}
