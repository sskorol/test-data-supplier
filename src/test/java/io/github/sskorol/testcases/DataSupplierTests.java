package io.github.sskorol.testcases;

import io.github.sskorol.listeners.InvokedMethodNameListener;
import one.util.streamex.EntryStream;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSupplierTests extends SimpleBaseTest {

    @Test
    public void arraysDataSuppliersShouldWork() {
        final InvokedMethodNameListener listener = run(ArraysDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
                .hasSize(6)
                .containsExactly(
                        "supplyCustomArrayData(User(name=username, password=password))",
                        "supplyCustomArrayData(null)",
                        "supplyExternalArrayData(User(name=user1, password=password1),User(name=user2, password=password2))",
                        "supplyExtractedArrayData(data1,data2)",
                        "supplyPrimitiveArrayData(0.1)",
                        "supplyPrimitiveArrayData(0.3)"
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
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.getNullCollectionData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.getNullStreamData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.extractNullObjectData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.getNullArrayData.",
                        "java.lang.IllegalArgumentException: Nothing to return from data supplier. The following test will be skipped: NullObjectsDataSupplierTests.getNullObjectData."
                );
    }
}