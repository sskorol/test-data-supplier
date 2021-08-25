package io.github.sskorol.core;

import io.github.sskorol.model.DataSupplierMetaData;
import one.util.streamex.StreamEx;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

import static io.github.sskorol.utils.ServiceLoaderUtils.load;
import static io.vavr.API.*;

/**
 * Key aspect for DataSupplier interception.
 */
@Aspect
public class DataSupplierAspect {

    private static final List<DataSupplierInterceptor> DATA_SUPPLIERS =
            load(DataSupplierInterceptor.class, DataSupplierAspect.class.getClassLoader());
    private static final List<IAnnotationTransformerInterceptor> ANNOTATION_TRANSFORMERS =
            load(IAnnotationTransformerInterceptor.class, DataSupplierAspect.class.getClassLoader());

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

    @Before("execution(* io.github.sskorol.core.DataProviderTransformer.transform(..))")
    public void beforeTransformationCall(final JoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();

        Match(args[0]).of(
                Case($(ITestAnnotation.class::isInstance), arg ->
                        run(() -> callTransformer(at -> at.transform((ITestAnnotation) arg,
                                (Class) args[1], (Constructor) args[2], (Method) args[3])))),
                Case($(IFactoryAnnotation.class::isInstance), arg ->
                        run(() -> callTransformer(at -> at.transform((IFactoryAnnotation) arg, (Method) args[1])))),
                Case($(IConfigurationAnnotation.class::isInstance), arg ->
                        run(() -> callTransformer(at -> at.transform((IConfigurationAnnotation) arg,
                                (Class) args[1], (Constructor) args[2], (Method) args[3])))),
                Case($(IDataProviderAnnotation.class::isInstance), arg ->
                        run(() -> callTransformer(at -> at.transform((IDataProviderAnnotation) arg,
                                (Method) args[1])))),
                Case($(IListenersAnnotation.class::isInstance), arg ->
                        run(() -> callTransformer(at -> at.transform((IListenersAnnotation) arg, (Class) args[1]))))
        );
    }

    public static List<DataSupplierInterceptor> getInterceptors() {
        return DATA_SUPPLIERS;
    }

    public static List<IAnnotationTransformerInterceptor> getTransformers() {
        return ANNOTATION_TRANSFORMERS;
    }

    private static void callTransformer(final Consumer<IAnnotationTransformerInterceptor> condition) {
        StreamEx.of(ANNOTATION_TRANSFORMERS).forEach(condition);
    }
}
