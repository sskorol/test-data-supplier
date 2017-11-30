# Test Data Supplier 

[![Build Status](https://travis-ci.org/sskorol/test-data-supplier.svg?branch=master)](https://travis-ci.org/sskorol/test-data-supplier)
[![codebeat badge](https://codebeat.co/badges/2d4e3080-1ec0-4747-b81e-06fa7d95e955)](https://codebeat.co/projects/github-com-sskorol-test-data-supplier-master)
[![codecov](https://codecov.io/gh/sskorol/test-data-supplier/branch/master/graph/badge.svg)](https://codecov.io/gh/sskorol/test-data-supplier)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier)
[![Bintray](https://api.bintray.com/packages/sskorol/test-data-supplier/test-data-supplier/images/download.svg)](https://bintray.com/sskorol/test-data-supplier/test-data-supplier/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://goo.gl/9GLmMZ)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/sskorol/test-data-supplier.svg?style=social)](https://twitter.com/intent/tweet?text=Check%20new%20Test%20Data%20Supplier%20library:&url=https://github.com/sskorol/test-data-supplier)

This repository contains TestNG **DataProvider** wrapper which helps to supply test data in a more flexible way.

Common **DataProvider** forces using quite old and ugly syntax which expects one of the following types to be returned from DP method's body:

 - Object[][]
 - Iterator<Object[]>

That's weird, as developers tend to use Stream and Collection API for data manipulation in the modern Java world.

Just imaging if you could use the following syntax to supply some filtered and sorted data into test method's signature:

```java
@DataSupplier
public Stream<User> getData() {
    return Stream.of(
        new User("Petya", "password2"),
        new User("Virus Petya", "password3"),
        new User("Mark", "password1"))
            .filter(u -> !u.getName().contains("Virus"))
            .sorted(comparing(User::getPassword));
}
    
@Test(dataProvider = "getData")
public void shouldSupplyStreamData(final User user) {
    // ...
}
```

Much better and flexible than two-dimensional arrays or iterators, isn't it?

And what if we don't want to iterate the same test N times depending on collection size? What if we want to extract its values and inject into test's signature like the following?

```java
@DataSupplier(transpose = true)
public List<User> getExtractedData() {
    return StreamEx.of(
        new User("username1", "password1"),
        new User("username2", "password2"))
            .toList();
}
        
@Test(dataProvider = "getExtractedData")
public void shouldSupplyExtractedListData(final User... users) {
    // ...
}
```

You can do even more, if you want to perform a Java-like **flatMap** operation for each row:

```java
@DataSupplier(flatMap = true)
public Map<Integer, String> getInternallyExtractedMapData() {
    return EntryStream.of(asList("user3", "user4")).toMap();
}
    
@Test(dataProvider = "getInternallyExtractedMapData")
public void supplyInternallyExtractedMapData(final Integer key, final String value) {
    // not implemented
}
```   

## Supported return types

 - Collection
 - Map
 - Entry
 - Object[]
 - double[]
 - int[]
 - long[]
 - Stream / StreamEx 
 - Tuple
 - A single Object of any common or custom type

## Usage

### Gradle

Add the following configuration into **build.gradle**:

```groovy
repositories {
    jcenter()
}
    
dependencies {
    compile('org.testng:testng:6.13.1',
            'io.github.sskorol:test-data-supplier:1.4.0'
    )
}
    
test {
    useTestNG() {
        listeners << 'io.github.sskorol.core.DataProviderTransformer'
    }
}
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-gradle-example) with usage examples.

### Maven

Add the following configuration into **pom.xml**:

```xml
<dependencies>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>6.13.1</version>
    </dependency>
    <dependency>
        <groupId>io.github.sskorol</groupId>
        <artifactId>test-data-supplier</artifactId>
        <version>1.4.0</version>
    </dependency>
</dependencies>
    
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.20.1</version>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>io.github.sskorol.core.DataProviderTransformer</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-maven-example) with usage examples.

### TestNG

If you're going to run tests directly from IDE via TestNG run configuration, you have to explicitly set 
**io.github.sskorol.core.DataProviderTransformer** into Listeners section:

![image](https://user-images.githubusercontent.com/6638780/32335197-fc3978ea-bff4-11e7-840b-01508615526d.png) 

Otherwise, you'll get an exception about missing **DataProvider**, as test listeners' specified in Gradle / Maven settings  
are ignored while using TestNG run configuration. And TestNG itself will try to locate methods with a common annotation, 
instead of **DataSupplier**. 

### API

Instead of a common **DataProvider** annotation use the following:
 
```java
@DataSupplier
public T getData() {
    //...
}
```

**DataSupplier** supports the following args: **name**, **transpose**, **flatMap**, **runInParallel** and **indices**. 

You can refer **DataSupplier** the same way as with TestNG **DataProvider**:

```java
@Test(dataProvider = "getData")
public void supplyData(final T data) {
    // ...
}
    
// or
    
@Test(dataProviderClass = ExternalDataProvider.class, dataProvider = "getData")
public void supplyExternalData(final T data) {
    // ...
}
```

Check **io.github.sskorol.testcases** package for more examples.

### Factory

In case of `@DataSupplier` usage along with `@Factory` annotation, it's required to explicitly provide `dataProviderClass` arg. 
Otherwise, you'll get an exception about missing `DataProvider`. That's a limitation caused by [TNG-1631](https://github.com/cbeust/testng/issues/1631).

```java
@NoArgsConstructor
public class InternalFactoryTests {
    
    @DataSupplier
    public StreamEx getConstructorData() {
        return IntStreamEx.rangeClosed(1, 3).boxed();
    }
    
    @DataSupplier
    public String getTestData() {
        return "data";
    }
    
    @Factory(dataProvider = "getConstructorData", dataProviderClass = InternalFactoryTests.class)
    public InternalFactoryTests(final int index) {
        // not implemented
    }
    
    @Test(dataProvider = "getTestData")
    public void internalFactoryTest(final String data) {
        // not implemented
    }
}
```

### Tracking meta-data

**DataSupplierInterceptor** interface allows tracking original **DataProvider** method calls for accessing additional meta-data. You can use the following snippet for getting required info:
```java
public class DataSupplierInterceptorImpl implements DataSupplierInterceptor {
    
    private static final Map<ITestNGMethod, DataSupplierMetaData> META_DATA = new ConcurrentHashMap<>();
    
    @Override
    public void beforeDataPreparation(final ITestContext context, final ITestNGMethod method) {
    }
    
    @Override
    public void afterDataPreparation(final ITestContext context, final ITestNGMethod method) {
    }
    
    @Override
    public void onDataPreparation(final DataSupplierMetaData testMetaData) {
        META_DATA.putIfAbsent(testMetaData.getTestMethod(), testMetaData);
    }
    
    @Override
    public Collection<DataSupplierMetaData> getMetaData() {
        return META_DATA.values();
    }
}
```

This class should be then loaded via SPI mechanism. Just create **META-INF/services** folder in **resources** root, and add a new file **io.github.sskorol.core.DataSupplierInterceptor** with a full path to implementation class.

## IntelliJ IDEA support

**Test Data Supplier** is integrated with IntelliJ IDEA in a form of plugin. Just install **test-data-supplier-plugin** from the official JetBrains repository.

More information about its features could be found on the related [GitHub](https://github.com/sskorol/test-data-supplier-plugin) page.
