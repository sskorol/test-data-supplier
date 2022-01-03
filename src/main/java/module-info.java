module io.github.sskorol.testdatasupplier {
    exports io.github.sskorol.core;
    exports io.github.sskorol.data;
    exports io.github.sskorol.model;
    exports io.github.sskorol.utils;

    opens io.github.sskorol.utils to org.jooq.joor;

    provides org.testng.ITestNGListener with io.github.sskorol.core.DataProviderTransformer;

    uses io.github.sskorol.core.IAnnotationTransformerInterceptor;
    uses io.github.sskorol.core.DataSupplierInterceptor;
    uses org.testng.ITestNGListener;

    requires org.testng;
    requires io.vavr;
    requires one.util.streamex;
    requires reflections;
    requires org.jooq.joor;
    requires org.aspectj.runtime;
    requires com.google.gson;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.databind;
    requires commons.csv;
    requires zerocell.core;
    requires static lombok;
}
