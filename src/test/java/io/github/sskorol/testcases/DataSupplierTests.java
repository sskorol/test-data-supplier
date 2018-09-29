package io.github.sskorol.testcases;

import io.github.sskorol.listeners.DummyListener;
import one.util.streamex.EntryStream;
import org.testng.ITestResult;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners(DummyListener.class)
public class DataSupplierTests extends TestNGRunner {

    @Test
    public void arraysDataSuppliersShouldWork() {
        var listener = run(ArraysDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(13)
            .containsExactly(
                "supplyCustomArrayData(User(name=username, password=password))",
                "supplyCustomArrayData(null)",
                "supplyExternalArrayData(User(name=user1, password=password1),User(name=user2, password=password2))",
                "supplyExtractedArrayData(data1,data2)",
                "supplyNestedArrayData(data3,data3)",
                "supplyNestedArrayData(data4,data4)",
                "supplyNestedArrayData(data5,data5)",
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
        var listener = run(CollectionsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(11)
            .containsExactly(
                "supplyCommonListData(data1)",
                "supplyCommonListData(data2)",
                "supplyCommonMapData(0=user1)",
                "supplyCommonMapData(1=user2)",
                "supplyCustomListData([null,User(name=username, password=password)])",
                "supplyExternalCollectionData(data1)",
                "supplyExternalCollectionData(data2)",
                "supplyInternallyExtractedMapData(0,user3)",
                "supplyInternallyExtractedMapData(1,user4)",
                "supplyTransposedInternallyExtractedMapData(0,user7,1,user8)",
                "supplyTransposedMapData(0=user5,1=user6)"
            );
    }

    @Test
    public void streamsDataSuppliersShouldWork() {
        var listener = run(StreamsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(20)
            .containsExactly(
                "supplyCustomStreamData(user2)",
                "supplyCustomStreamData(user3)",
                "supplyExternalStreamData(1)",
                "supplyExternalStreamData(3)",
                "supplyExternalStreamData(5)",
                "supplyExternalStreamData(7)",
                "supplyExternalStreamData(9)",
                "supplyExtractedCustomStreamData(User(name=Mark, password=password1),User(name=Petya, password=password2))",
                "supplyFilteredByIndicesData(0)",
                "supplyFilteredByIndicesData(2)",
                "supplyFilteredByIndicesData(4)",
                "supplyFilteredByIndicesData(6)",
                "supplyFilteredByIndicesData(8)",
                "supplyInternallyExtractedStreamData(name1,password1)",
                "supplyInternallyExtractedStreamData(name2,password2)",
                "supplyPrimitiveStreamData(0)",
                "supplyPrimitiveStreamData(2)",
                "supplyPrimitiveStreamData(4)",
                "supplyPrimitiveStreamData(6)",
                "supplyPrimitiveStreamData(8)"
            );
    }

    @Test
    public void singleObjectDataSuppliersShouldWork() {
        var listener = run(SingleObjectsDataSupplierTests.class);

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
        var listener = run(NullObjectsDataSupplierTests.class);

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
            .allMatch(message -> message.equals(
                "java.lang.IllegalArgumentException: Nothing to return from data supplier. Test will be skipped.")
            );
    }

    @Test
    public void dataSuppliersWithInjectedArgsShouldWork() {
        var listener = run(InjectedArgsDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(5)
            .containsExactly(
                "supplyContextMetaData(DataSupplier tests)",
                "supplyFullMetaData(DataSupplier tests,supplyFullMetaData,test description)",
                "supplyITestNGMethodMetaData(supplyITestNGMethodMetaData)",
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
                "java.lang.IllegalArgumentException: Nothing to return from data supplier. Test will be skipped.");
    }

    @Test
    public void missingDataSuppliersShouldNotWork() {
        var listener = run(MissingDataSupplierTests.class);

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
        var listener = run(CommonDataProviderTests.class);

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
        var listener = run(DataSupplierWithCustomNamesTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(3)
            .containsExactly(
                "supplyExternalPasswordFromNamedDataSupplier(qwerty)",
                "supplyStringFromNamedParentClassDataSupplier(baseData)",
                "supplyUserFromNamedDataSupplier(User(name=userFromNamedDataSupplier, password=password))"
            );
    }

    @Test
    public void tupleDataSupplierTestsShouldWork() {
        var listener = run(TupleDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(6)
            .containsExactly(
                "supplyCommonTupleData(data1)",
                "supplyCommonTupleData(data2)",
                "supplyExternalTupleData(1,2,3.0)",
                "supplyExtractedTupleData(1,User(name=name, password=password))",
                "supplyInternallyExtractedTupleData(data1,data3)",
                "supplyInternallyExtractedTupleData(data2,data4)"
            );
    }

    @Test
    public void parallelDataSupplierTestsShouldWork() {
        var listener = run(ParallelDataSupplierTests.class);

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

    @Test
    public void dataSupplierWithClassLevelAnnotationsShouldWork() {
        var listener = run(
            ClassLevelAnnotationWithLocalDataSupplierTests.class,
            ClassLevelAnnotationWithDataSuppliersTests.class
        );

        assertThat(listener.getSucceedMethodNames())
            .hasSize(4)
            .containsExactly(
                "shouldBeExecutedWithClassLevelAnnotationWithoutDataSupplier()",
                "shouldBeExecutedWithLocalDataSupplier(data)",
                "shouldBeExecutedWithClassLevelAnnotationWithDataSupplier(data1)",
                "shouldBeExecutedWithExternalDataSupplier(data2)"
            );
    }

    @Test
    public void dataSupplierWithParentClassLevelAnnotationsShouldWork() {
        var listener = run(ChildTest.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(3)
            .containsExactly(
                "dummyTest()",
                "provideDataFromParentClassDataSupplier(hash1,password1)",
                "provideDataFromParentClassDataSupplier(hash2,password2)"
            );
    }

    @Test
    public void dataSupplierWithFactoryAnnotationShouldWork() {
        var listener = run(InternalFactoryTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(3)
            .containsExactly(
                "internalFactoryTest(data)",
                "internalFactoryTest(data)",
                "internalFactoryTest(data)"
            );
    }

    @Test
    public void dataSupplierWithExternalFactoryAnnotationShouldWork() {
        var listener = run(ExternalFactorySource.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(2)
            .containsExactly(
                "externalFactoryTest()",
                "externalFactoryTest()"
            );
    }

    @Test
    public void dataSupplierWithIncompleteFactoryAnnotationShouldBeExecuted() {
        var listener = run(IncompleteFactoryTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(5)
            .containsExactly(
                "incompleteFactoryTest()",
                "incompleteFactoryTest()",
                "incompleteFactoryTest()",
                "incompleteFactoryTest()",
                "incompleteFactoryTest()"
            );
    }

    @Test
    public void csvDataSupplierTestsShouldWork() {
        var listener = run(CsvDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(4)
            .containsExactly(
                "shouldReadLocalCsv(User(name=admin, password=admin))",
                "shouldReadLocalCsv(User(name=sskorol, password=password))",
                "shouldReadLocalCsv(User(name=guest, password=123))",
                "shouldReadRemoteCsv(CrimeRecord(address=3108 OCCIDENTAL DR, description=10851(A)VC TAKE VEH W/O OWNER))"
            );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldNotBeExecutedWithMissingCsvResource()");
    }

    @Test
    public void jsonDataSupplierTestsShouldWork() {
        var listener = run(JsonDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(7)
            .containsExactly(
                "shouldReadLocalJson(Client(firstName=Ivan, lastName=Ivanov))",
                "shouldReadLocalJsonArray(JsonUser(name=admin, password=admin))",
                "shouldReadLocalJsonArray(JsonUser(name=sskorol, password=password))",
                "shouldReadLocalJsonArray(JsonUser(name=guest, password=123))",
                "shouldReadRemoteJson(Animal(name=Meowsy, species=cat, foods=Food(likes=[tuna, catnip], dislikes=[ham, zucchini])))",
                "shouldReadRemoteJson(Animal(name=Barky, species=dog, foods=Food(likes=[bones, carrots], dislikes=[tuna])))",
                "shouldReadRemoteJson(Animal(name=Purrpaws, species=cat, foods=Food(likes=[mice], dislikes=[cookies])))"
            );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldNotBeExecutedWithMissingJsonResource()");
    }

    @Test
    public void ymlDataSupplierTestsShouldWork() {
        var listener = run(YamlDataSupplierTests.class);

        assertThat(listener.getSucceedMethodNames())
            .hasSize(5)
            .containsExactly(
                "shouldReadMultiLocalYamlData(YamlUser(name=admin, password=admin))",
                "shouldReadMultiLocalYamlData(YamlUser(name=sskorol, password=password))",
                "shouldReadMultiLocalYamlData(YamlUser(name=guest, password=123))",
                "shouldReadRemoteYamlData(TravisConfiguration(language=java, sudo=false, install=true, addons={sonarcloud=Addon(organization=sskorol-github, token=Token(secure=G0io5DQn6jsZS58Nk04xMP8b6RmdOSrR5LaFnfHTeVNDhy1KBVt5/pSQL7od9S0o9UreZSidZ9FXKSTWQxmkJJ/RZIsw91DX2nWwRCqIL5XqRi+sKHjnToeJUmn5HuAYnAQ6NoSg5l9HryXEX6PG2n15hRBUjjpigOu+W4gL7JCpYlIQ/FcvzaEnw0n4APGEoldTpfSUHbnbzWiTFFB9Cyy4ke/DwBiabKwTCAWwzXXeXRKIFYnDqITKapx0kNRlZyq/nElHmd0q29Y4zuvd2YFB/vXuFhtxo2lNJF6kvfO1hrWqPlkvnGUsOa12Ko6zurpilmS3GCjasYjHsPFzqQtdCFSmkmP4goULAT9sUSd2X7bzkpTR8JnxYdeOvvB5D7aBJvwkkfUj12ClCCIBaAhRzBBrjSWRlmZsWh/0gbXLMZiwL44QwRnnTNmpfSb1gvWvTgTnlg6QTb7TZ/3g0a7zRAtkwuB470XE9W7aDa8qVYC9njZIB2M8vVcKNJlDUhDPIf+XJHQb/vlzZJeJjE6SE9rODi7SICwkSCvb6+G7Nz1Vb/9TfzZLbylGgtr/cLOGaccbAt5c84BxzsZnsgYGh+IhLbPqfKOcIAhfCBaJ0G8lCPMuTtkWaicV5QzxTc/yYSO1G0fyT/6PCOpN6//S+pN8rZjMFyWr7fwR3oo=))}, jdk=[oraclejdk10], script=[./gradlew report sonarqube], cache={directories=[$HOME/.m2/repository, $HOME/.sonar/cache, $HOME/.gradle, .gradle]}, notifications={email=Email(recipients=[serhii.s.korol@gmail.com])}))",
                "shouldReadSingleLocalYamlData(DockerConfiguration(version=3, services={react-app=ServiceConfiguration(image=sskorol/sc18-react-app, networkMode=bridge, expose=[3000], ports=null, environment=null, links=null, volumes=null, command=null), selenoid=ServiceConfiguration(image=aerokube/selenoid, networkMode=bridge, expose=null, ports=[4444:4444], environment=[DOCKER_API_VERSION=1.35], links=[react-app], volumes=[${WORK_DIR}:/etc/selenoid, //var/run/docker.sock:/var/run/docker.sock], command=[-conf, /etc/selenoid/browsers.json])}))"
            );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldNotBeExecutedWithMissingYamlResource()");
    }
}
