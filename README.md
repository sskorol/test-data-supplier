# Test Data Supplier 

[![Build Status](https://travis-ci.org/sskorol/test-data-supplier.svg?branch=master)](https://travis-ci.org/sskorol/test-data-supplier)
[![codebeat badge](https://codebeat.co/badges/2d4e3080-1ec0-4747-b81e-06fa7d95e955)](https://codebeat.co/projects/github-com-sskorol-test-data-supplier-master)
[![codecov](https://codecov.io/gh/sskorol/test-data-supplier/branch/master/graph/badge.svg)](https://codecov.io/gh/sskorol/test-data-supplier)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier)
[![Bintray](https://api.bintray.com/packages/sskorol/test-data-supplier/test-data-supplier/images/download.svg)](https://bintray.com/sskorol/test-data-supplier/test-data-supplier/_latestVersion)
[![Dependency Status](https://www.versioneye.com/user/projects/59615dee6725bd0048edfa2f/badge.svg?style=flat)](https://www.versioneye.com/user/projects/59615dee6725bd0048edfa2f)
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
@DataSupplier(extractValues = true)
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

## Supported return types

 - Collection
 - Object[]
 - double[]
 - int[]
 - long[]
 - Stream
 - StreamEx
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
    compile('org.testng:testng:6.11',
            'io.github.sskorol:test-data-supplier:1.0.0'
    )
}
    
test {
    useTestNG() {
        listeners << 'io.github.sskorol.dataprovider.DataProviderTransformer'
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
        <version>6.11</version>
    </dependency>
    <dependency>
        <groupId>io.github.sskorol</groupId>
        <artifactId>test-data-supplier</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
    
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>2.20</version>
            <configuration>
                <properties>
                    <property>
                        <name>listener</name>
                        <value>io.github.sskorol.dataprovider.DataProviderTransformer</value>
                    </property>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-maven-example) with usage examples.

### API

Instead of a common **DataProvider** annotation use the following:
 
```java
@DataSupplier
public T getData() {
    //...
}
```

**DataSupplier** supports the following args: **name**, **extractValues** and **runInParallel**. 

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

### Limitations

 - no **indices** arg support (could be achieved by Stream API usage);
 - only **ITestContext** / **Method** injections are supported;
 - missing DataProvider warning (affected by TestNG inspections);
 - unused method warning (could be suppressed in IDE):
 
 ![image](https://user-images.githubusercontent.com/6638780/27763889-13dd0b5e-5e95-11e7-8c19-719c6a3a15d9.png)
