package io.github.sskorol.testcases;

import io.github.sskorol.listeners.InvokedMethodNameListener;
import one.util.streamex.EntryStream;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSupplierTests extends BaseTest {

    @Test
    public void arraysDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(ArraysDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(10)
                .containsExactly(
                        "supplyCustomArrayData(User(name=username, password=password))",
                        "supplyCustomArrayData(null)",
                        "supplyExternalArrayData(User(name=user1, password=password1),User(name=user2, password=password2))",
                        "supplyExtractedArrayData(data1,data2)",
                        "supplyPrimitiveDoubleArrayData(0.1)",
                        "supplyPrimitiveDoubleArrayData(0.3)",
                        "supplyPrimitiveIntArrayData(5)",
                        "supplyPrimitiveLongArrayData(2)",
                        "supplyPrimitiveLongArrayData(6)",
                        "supplyPrimitiveLongArrayData(100)"
                );
    }

    @Test
    public void collectionsDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(CollectionsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(5)
                .containsExactly(
                        "supplyCommonListData(data1)",
                        "supplyCommonListData(data2)",
                        "supplyCustomListData([null,User(name=username, password=password)])",
                        "supplyExternalCollectionData(data1)",
                        "supplyExternalCollectionData(data2)"
                );
    }

    @Test
    public void streamsDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(StreamsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(13)
                .containsExactly(
                        "supplyCustomStreamData(user2)",
                        "supplyCustomStreamData(user3)",
                        "supplyExternalStreamData(1)",
                        "supplyExternalStreamData(3)",
                        "supplyExternalStreamData(5)",
                        "supplyExternalStreamData(7)",
                        "supplyExternalStreamData(9)",
                        "supplyExtractedCustomStreamData(User(name=Mark, password=password1),User(name=Petya, password=password2))",
                        "supplyPrimitiveStreamData(0)",
                        "supplyPrimitiveStreamData(2)",
                        "supplyPrimitiveStreamData(4)",
                        "supplyPrimitiveStreamData(6)",
                        "supplyPrimitiveStreamData(8)"
                );
    }

    @Test
    public void singleObjectDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(SingleObjectsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(4)
                .containsExactly(
                        "supplyCommonObject(data1)",
                        "supplyCustomObjectData(User(name=username, password=password))",
                        "supplyExternalObjectData(0.1)",
                        "supplyPrimitiveData(5)"
                );
    }

    @Test
    public void nullObjectsDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(NullObjectsDataSupplierTests.class);

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
                .hasSize(5)
                .containsExactly(
                        "supplyExtractedNullObject()",
                        "supplyNullArrayData()",
                        "supplyNullCollectionData()",
                        "supplyNullObjectData()",
                        "supplyNullStreamData()"
                );

        assertThat(EntryStream.of(listener.getResults()).values().toList())
                .extracting(ITestResult::getThrowable)
                .extracting(Throwable::getMessage)
                .containsExactly(
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.supplyNullCollectionData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.supplyNullStreamData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.supplyExtractedNullObject.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.supplyNullArrayData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.supplyNullObjectData."
                );
    }

    @Test
    public void dataSuppliersWithInjectedArgsShouldWork() {
        final InvokedMethodNameListener listener = run(InjectedArgsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(4)
                .containsExactly(
                        "supplyContextMetaData(DataSupplier tests)",
                        "supplyFullMetaData(DataSupplier tests,supplyFullMetaData)",
                        "supplyMethodMetaData(supplyMethodMetaData)",
                        "supplyWrongArgTypeMethodMetaData(data)"
                );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
                .hasSize(1)
                .containsExactly("supplyNullArgTypeMethodMetaData()");

        assertThat(EntryStream.of(listener.getResults()).values().toList())
                .filteredOn(r -> r.getStatus() == ITestResult.SKIP)
                .extracting(ITestResult::getThrowable)
                .extracting(Throwable::getMessage)
                .containsExactly(
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: InjectedArgsDataSupplierTests.supplyNullArgTypeMethodMetaData.");
    }

    @Test
    public void missingDataSuppliersShouldNotWork() {
        final InvokedMethodNameListener listener = run(MissingDataSupplierTests.class);

        assertThat(listener.getFailedBeforeInvocationMethodNames())
                .hasSize(2)
                .containsExactly(
                        "failOnDataSupplying()",
                        "failOnExternalDataSupplying()"
                );

        assertThat(EntryStream.of(listener.getResults()).values().toList())
                .extracting(ITestResult::getThrowable)
                .extracting(t -> t.getMessage().trim())
                .containsExactly(
                        "Method public void io.github.sskorol.testcases.MissingDataSupplierTests.failOnDataSupplying() requires a @DataProvider named : missingDataSupplier",
                        "Method public void io.github.sskorol.testcases.MissingDataSupplierTests.failOnExternalDataSupplying() requires a @DataProvider named : missingExternalDataSupplier in class io.github.sskorol.datasuppliers.ExternalDataSuppliers"
                );
    }

    @Test
    public void commonDataProviderTestsShouldWork() {
        final InvokedMethodNameListener listener = run(CommonDataProviderTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(3)
                .containsExactly(
                        "shouldPassWithCommonDataProvider(data)",
                        "shouldPassWithExternalDataProvider(data)",
                        "shouldPassWithoutDataProvider()"
                );
    }

    @Test
    public void namedDataSupplierTestsShouldWork() {
        final InvokedMethodNameListener listener = run(DataSupplierWithCustomNamesTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(2)
                .containsExactly(
                        "supplyExternalPasswordFromNamedDataSupplier(qwerty)",
                        "supplyUserFromNamedDataSupplier(User(name=userFromNamedDataSupplier, password=password))"
                );
    }

    @Test
    public void tupleDataSupplierTestsShouldWork() {
        final InvokedMethodNameListener listener = run(TupleDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(4)
                .containsExactly(
                        "supplyCommonTupleData(data1)",
                        "supplyCommonTupleData(data2)",
                        "supplyExternalTupleData(1,2,3.0)",
                        "supplyExtractedTupleData(1,User(name=name, password=password))"
                );
    }

    @Test
    public void parallelDataSupplierTestsShouldWork() {
        final InvokedMethodNameListener listener = run(ParallelDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        "supplyParallelData(data1)",
                        "supplyParallelData(data2)",
                        "supplySeqData(data1)",
                        "supplySeqData(data2)"
                );

        assertThat(EntryStream.of(listener.getThreads())
                              .values()
                              .flatMap(Collection::stream)
                              .distinct()
                              .toList())
                .hasSize(3);
    }
}