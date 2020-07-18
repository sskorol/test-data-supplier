package io.github.sskorol.utils;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.FieldName;
import io.github.sskorol.data.Source;
import io.github.sskorol.model.TypeMappings;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import one.util.streamex.StreamEx;
import org.reflections.Reflections;
import org.testng.ITestNGMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import org.testng.internal.annotations.IDataProvidable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.ClassLoader.getSystemResource;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.joor.Reflect.onClass;

/**
 * An utility class for internal DataSupplier management.
 */
@SuppressWarnings("FinalLocalVariable")
@UtilityClass
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getDataSupplierClass(final IDataProvidable annotation, final Class<T> testClass,
                                                    final Method testMethod) {
        return ofNullable(annotation.getDataProviderClass())
                .map(dataProviderClass -> (Class) dataProviderClass)
                .orElseGet(() -> findParentDataSupplierClass(testMethod, testClass));
    }

    public static Method getDataSupplierMethod(final Class<?> targetClass, final String targetMethodName) {
        var methodMetaData = StreamEx.of(targetClass.getMethods())
                .map(method -> Tuple.of(method, method.getDeclaredAnnotation(DataSupplier.class)))
                .filter(hasDataSupplierMethod(targetMethodName))
                .map(metaData -> Tuple.of(metaData._1.getName(), metaData._1.getParameterTypes()))
                .findFirst()
                .orElseGet(() -> Tuple.of(targetMethodName, new Class<?>[0]));

        return Try.of(() -> targetClass.getMethod(methodMetaData._1, methodMetaData._2)).get();
    }

    public static DataSupplier getDataSupplierAnnotation(final Class<?> targetClass, final String targetMethodName) {
        return Try.of(() -> getDataSupplierMethod(targetClass, targetMethodName))
                .map(method -> method.getDeclaredAnnotation(DataSupplier.class))
                .filter(Objects::nonNull)
                .getOrElse((DataSupplier) null);
    }

    public static Object invokeDataSupplier(final Tuple2<Method, Object[]> methodMetaData) {
        return onClass(methodMetaData._1.getDeclaringClass())
                .create()
                .call(methodMetaData._1.getName(), methodMetaData._2)
                .get();
    }

    public static Method findDataSupplier(final ITestNGMethod testMethod) {
        var annotationMetaData = testMethod.isTest()
                ? getTestAnnotationMetaData(testMethod)
                : getFactoryAnnotationMetaData(testMethod);
        return getDataSupplierMethod(annotationMetaData._1, annotationMetaData._2);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T[]> castToArray(final Class<T> entityClass) {
        return (Class<T[]>) (entityClass.isArray() ? entityClass : Array.newInstance(entityClass, 1).getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> castToObject(final Class<T> entityClass) {
        return (Class<T>) (entityClass.isArray() ? entityClass.getComponentType() : entityClass);
    }

    public static String getFieldName(final Field field) {
        var fieldAnnotation = field.getDeclaredAnnotation(FieldName.class);
        return isNull(fieldAnnotation) ? field.getName() : fieldAnnotation.value();
    }

    public static <T> URL getSourcePath(final Class<T> entity) throws IOException {
        return getSourcePath(ofNullable(entity.getDeclaredAnnotation(Source.class))
                .map(Source::path)
                .orElse(""));
    }

    public static URL getSourcePath(final String path) throws IOException {
        return ofNullable(Try.of(() -> new URL(path)).getOrElseGet(ex -> getSystemResource(path)))
                .orElseThrow(() -> new IOException("Unable to access resource specified by " + path + " path"));
    }

    public static <T> StreamEx<T> streamOf(final T data) {
        if (isNull(data)) {
            throw new IllegalArgumentException("Nothing to return from data supplier. Test will be skipped.");
        }

        return StreamEx.of(TypeMappings.values())
                .findFirst(type -> type.isInstanceOf(data))
                .map(type -> type.streamOf(data))
                .orElseGet(() -> StreamEx.of(data));
    }

    @SuppressWarnings("unchecked")
    private static Tuple2<Class<?>, String> getTestAnnotationMetaData(final ITestNGMethod testMethod) {
        var declaringClass = testMethod.getConstructorOrMethod().getDeclaringClass();
        var parentClass = findParentDataSupplierClass(testMethod.getConstructorOrMethod().getMethod(), declaringClass);
        var testAnnotation = ofNullable(testMethod.getConstructorOrMethod()
                .getMethod()
                .getDeclaredAnnotation(Test.class))
                .orElseGet(() -> declaringClass.getDeclaredAnnotation(Test.class));
        var dataSupplierClass = ofNullable(testAnnotation)
                .map(Test::dataProviderClass)
                .filter(dp -> dp != Object.class)
                .orElse((Class) parentClass);

        return Tuple.of(dataSupplierClass, testAnnotation.dataProvider());
    }

    @SuppressWarnings("unchecked")
    private static Tuple2<Class<?>, String> getFactoryAnnotationMetaData(final ITestNGMethod testMethod) {
        var constructor = testMethod.getConstructorOrMethod().getConstructor();
        var method = testMethod.getConstructorOrMethod().getMethod();

        var factoryAnnotation = nonNull(method)
                ? ofNullable(method.getDeclaredAnnotation(Factory.class))
                : ofNullable(constructor.getDeclaredAnnotation(Factory.class));

        var dataProviderClass = factoryAnnotation
                .map(fa -> (Class) fa.dataProviderClass())
                .filter(cl -> cl != Object.class)
                .orElseGet(() -> testMethod.getConstructorOrMethod().getDeclaringClass());

        var dataProviderMethod = factoryAnnotation.map(Factory::dataProvider).orElse("");

        return Tuple.of(dataProviderClass, dataProviderMethod);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> findParentDataSupplierClass(final Method testMethod, final Class<T> testClass) {
        return (Class<T>) ofNullable(testMethod)
                .map(m -> Tuple.of(m, new Reflections(m.getDeclaringClass().getPackage().getName())))
                .map(findParentDataSupplierClass())
                .orElse(testClass);
    }

    private static Function<Tuple2<Method, Reflections>, Class<?>> findParentDataSupplierClass() {
        return t -> StreamEx.of(t._2.getSubTypesOf(t._1.getDeclaringClass()))
                .findFirst(c -> c.isAnnotationPresent(Test.class))
                .map(c -> c.getDeclaredAnnotation(Test.class))
                .map(a -> (Class) a.dataProviderClass())
                .orElse(t._1.getDeclaringClass());
    }

    private static Predicate<Tuple2<Method, DataSupplier>> hasDataSupplierMethod(final String targetMethodName) {
        return metaData -> nonNull(metaData._2)
                && (metaData._2.name().equals(targetMethodName) || metaData._1.getName().equals(targetMethodName));
    }
}
