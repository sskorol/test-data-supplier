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
            .hasSize(14)
            .containsExactly(
                "supplyCustomArrayData(User(name=username, password=password))",
                "supplyCustomArrayData(null)",
                "supplyCustomTransposedExtractedArrayData(User(name=username1, password=password1),User" +
                "(name=username2, password=password2))",
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
            .hasSize(15)
            .containsExactlyInAnyOrder(
                "supplyCommonListData(data1)",
                "supplyCommonListData(data2)",
                "supplyCommonMapData(0=user1)",
                "supplyCommonMapData(1=user2)",
                "supplyCustomListData([null,User(name=username, password=password)])",
                "supplyExternalCollectionData(data1)",
                "supplyExternalCollectionData(data2)",
                "supplyInternallyExtractedMapData(0,user3)",
                "supplyInternallyExtractedMapData(1,user4)",
                "supplyInternallyExtractedListData(data1)",
                "supplyInternallyExtractedListData(data2)",
                "supplyTransposedInternallyExtractedListData(data2,data1)",
                "supplyTransposedInternallyExtractedMapData(0=user7,1=user8)",
                "supplyTransposedListData([data1, data2])",
                "supplyTransposedMapData({0=user5, 1=user6})"
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
                "supplyExtractedCustomStreamData(User(name=Mark, password=password1),User(name=Petya, " +
                "password=password2))",
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
                "Method public void io.github.sskorol.testcases.MissingDataSupplierTests.failOnDataSupplying() " +
                "requires a @DataProvider named : missingDataSupplier",
                "Method public void io.github.sskorol.testcases.MissingDataSupplierTests.failOnExternalDataSupplying" +
                "() requires a @DataProvider named : missingExternalDataSupplier in class io.github.sskorol" +
                ".datasuppliers.ExternalDataSuppliers"
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
            .hasSize(6)
            .containsExactlyInAnyOrder(
                "supplyParallelData(data1)",
                "supplyParallelData(data2)",
                "supplyParallelDataWithErrorPropagation(data1)",
                "supplyParallelDataWithErrorPropagation(data2)",
                "supplySeqData(data1)",
                "supplySeqData(data2)"
            );

        assertThat(EntryStream.of(listener.getThreads())
                       .values()
                       .flatMap(Collection::stream)
                       .distinct()
                       .toList())
            .hasSize(5);
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
        var listener = run(ExternalFactorySourceTests.class);

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
                "shouldReadRemoteCsv(CrimeRecord(address=3108 OCCIDENTAL DR, description=10851(A)VC TAKE VEH W/O " +
                "OWNER))"
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
                "shouldReadRemoteJson(Animal(name=Meowsy, species=cat, foods=Food(likes=[tuna, catnip], " +
                "dislikes=[ham, zucchini])))",
                "shouldReadRemoteJson(Animal(name=Barky, species=dog, foods=Food(likes=[bones, carrots], " +
                "dislikes=[tuna])))",
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
                "shouldReadRemoteYamlData(TravisConfiguration(language=java, sudo=false, install=true, " +
                "addons={sonarcloud=Addon(organization=sskorol-github, token=null)}, jdk=[openjdk11], script=[" +
                "./gradlew clean test jacocoTestReport sonarqube], cache={directories=[$HOME/.m2/repository, $HOME/" +
                ".sonar/cache, $HOME/.gradle, .gradle]}, notifications=null))",
                "shouldReadSingleLocalYamlData(DockerConfiguration(version=3, " +
                "services={react-app=ServiceConfiguration(image=sskorol/sc18-react-app, networkMode=bridge, " +
                "expose=[3000], ports=null, environment=null, links=null, volumes=null, command=null), " +
                "selenoid=ServiceConfiguration(image=aerokube/selenoid, networkMode=bridge, expose=null, " +
                "ports=[4444:4444], environment=[DOCKER_API_VERSION=1.35], links=[react-app], " +
                "volumes=[${WORK_DIR}:/etc/selenoid, //var/run/docker.sock:/var/run/docker.sock], command=[-conf, " +
                "/etc/selenoid/browsers.json])}))"
            );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldNotBeExecutedWithMissingYamlResource()");
    }

    @Test
    public void xlsxDataSupplierTestsShouldWork() {
        var listener = run(XlsxDataSupplierTests.class);
        assertThat(listener.getSucceedMethodNames())
            .hasSize(27)
            .containsExactlyInAnyOrder(
                "shouldReadLocalExcelSpreadsheetWithoutSheet(PersonWithoutSheet(firstName=Zikani, " +
                "lastName=Nyirenda, dateOfBirth=1993-04-03))",
                "shouldReadLocalExcelSpreadsheetWithoutSheet(PersonWithoutSheet(firstName=Andrew, " +
                "lastName=Mfune, dateOfBirth=1994-05-21))",
                "shouldReadLocalExcelSpreadsheetWithoutSheet(PersonWithoutSheet(firstName=Blessings, " +
                "lastName=Mwafulirwa, dateOfBirth=1988-02-25))",
                "shouldReadLocalExcelSpreadsheetWithoutSheet(PersonWithoutSheet(firstName=Kondwani, " +
                "lastName=Chikhula, dateOfBirth=1986-04-05))",
                "shouldReadLocalExcelSpreadsheetWithoutSheet(PersonWithoutSheet(firstName=Moses, " +
                "lastName=Mpulula, dateOfBirth=1992-01-10))",
                "shouldReadLocalExcelSpreadsheetWithSheet(PersonWithSheet(firstName=Zikani, lastName=Nyirenda," +
                " dateOfBirth=1993-04-03))",
                "shouldReadLocalExcelSpreadsheetWithSheet(PersonWithSheet(firstName=Andrew, lastName=Mfune, " +
                "dateOfBirth=1994-05-21))",
                "shouldReadLocalExcelSpreadsheetWithSheet(PersonWithSheet(firstName=Blessings, " +
                "lastName=Mwafulirwa, dateOfBirth=1988-02-25))",
                "shouldReadLocalExcelSpreadsheetWithSheet(PersonWithSheet(firstName=Kondwani, " +
                "lastName=Chikhula, dateOfBirth=1986-04-05))",
                "shouldReadLocalExcelSpreadsheetWithSheet(PersonWithSheet(firstName=Moses, lastName=Mpulula, " +
                "dateOfBirth=1992-01-10))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test1, browser=chrome, " +
                "date=2022-05-19, nextCheck=2022-06-11T10:22:33, price=0.22, shouldRun=true, age=20, " +
                "phoneNumber=+919876543210, techStack=[selenium, appium, restassured]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test2, browser=edge, " +
                "date=2022-04-09, nextCheck=2022-05-21T11:02:13, price=3.55, shouldRun=false, age=23, " +
                "phoneNumber=+919876543210, techStack=[docker, k8s, openshift]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test3, browser=chrome, " +
                "date=2021-08-12, nextCheck=2023-02-01T00:00, price=0.0, shouldRun=true, age=21, phoneNumber=+919876543210," +
                " techStack=[postman, soapui]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test4, browser=chrome, " +
                "date=2023-01-01, nextCheck=2022-12-11T11:32:32, price=-1.24, shouldRun=true, age=22, " +
                "phoneNumber=+919876543210, techStack=[automation, performance, security]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test5, browser=chromium, " +
                "date=2022-02-28, nextCheck=2022-04-04T12:22:33, price=1000.0, shouldRun=true, age=101, " +
                "phoneNumber=+918291239893, techStack=[allure, testng, java, gradle]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test6, browser=firefox, " +
                "date=2022-05-19, nextCheck=2022-06-11T10:22:33, price=0.22, shouldRun=true, age=20, " +
                "phoneNumber=+919876543210, techStack=[selenium, appium, restassured]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test7, browser=chrome, " +
                "date=2022-04-09, nextCheck=2022-05-21T11:02:13, price=3.55, shouldRun=false, age=23, " +
                "phoneNumber=+919876543210, techStack=[docker, k8s, openshift]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test8, browser=firefox, " +
                "date=2021-08-12, nextCheck=2023-02-01T00:00, price=0.0, shouldRun=true, age=21, " +
                "phoneNumber=+919876543210, techStack=[postman, soapui]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test9, browser=edge, " +
                "date=2023-01-01, nextCheck=2022-12-11T11:32:32, price=-1.24, shouldRun=true, age=22, " +
                "phoneNumber=+919876543210, techStack=[automation, performance, security]))",
                "shouldReadLocalExcelSpreadsheetWithCustomConverters(XlsxData(testCase=test10, browser=firefox, " +
                "date=2022-02-28, nextCheck=2022-04-04T12:22:33, price=1000.0, shouldRun=true, age=101, " +
                "phoneNumber=+918291239893, techStack=[allure, testng, java, gradle]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test6, browser=firefox, " +
                "date=2022-05-19, nextCheck=2022-06-11T10:22:33, price=0.22, shouldRun=true, age=20, " +
                "phoneNumber=+919876543210, techStack=[selenium, appium, restassured]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test7, browser=chrome, " +
                "date=2022-04-09, nextCheck=2022-05-21T11:02:13, price=3.55, shouldRun=false, age=23, " +
                "phoneNumber=+919876543210, techStack=[docker, k8s, openshift]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test8, browser=firefox, " +
                "date=2021-08-12, nextCheck=2023-02-01T00:00, price=0.0, shouldRun=true, age=21, " +
                "phoneNumber=+919876543210, techStack=[postman, soapui]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test9, browser=edge, " +
                "date=2023-01-01, nextCheck=2022-12-11T11:32:32, price=-1.24, shouldRun=true, age=22, " +
                "phoneNumber=+919876543210, techStack=[automation, performance, security]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test10, browser=firefox, " +
                "date=2022-02-28, nextCheck=2022-04-04T12:22:33, price=1000.0, shouldRun=true, age=101, " +
                "phoneNumber=+918291239893, techStack=[allure, testng, java, gradle]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test11, browser=edge, " +
                "date=2022-05-19, nextCheck=2022-06-11T10:22:33, price=0.24, shouldRun=true, age=20, " +
                "phoneNumber=+919876543210, techStack=[selenium, appium, restassured]))",
                "shouldReadLocalExcelSpreadsheetWithCustomSheets(XlsxData(testCase=test12, browser=firefox, " +
                "date=2022-04-09, nextCheck=2022-05-21T11:02:13, price=2.12, shouldRun=false, age=23, " +
                "phoneNumber=+919876543210, techStack=[docker, k8s, openshift]))"
            );

        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldNotBeExecutedWithMissingXlsxResource()");
    }

    @Test
    public void dataSupplierWithErrorPropagationShouldWork() {
        var listener = run(DataSupplierWithErrorPropagationTests.class);
        assertThat(listener.getFailedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldPropagateTestFailure()");
        assertThat(listener.getSkippedBeforeInvocationMethodNames())
            .hasSize(1)
            .containsExactly("shouldSkipTest()");
    }
}
