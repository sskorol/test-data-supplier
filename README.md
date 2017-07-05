### Extended TestNG DataProvider

[![Maven Central](https://img.shields.io/badge/Maven%20Central-0.7.0-blue.svg?style=flat)](https://goo.gl/Lt9793)
[![Bintray](https://img.shields.io/badge/Bintray-0.7.0-brightgreen.svg?style=flat)](https://goo.gl/5b5R5a)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://goo.gl/9GLmMZ)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/sskorol/test-data-supplier.svg?style=social)](https://twitter.com/intent/tweet?text=Wow:&url=%5Bobject%20Object%5D)

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

#### Supported return types

 - Collection
 - Object[]
 - double[]
 - int[]
 - long[]
 - Stream
 - StreamEx
 - A single Object of any common or custom type

#### Usage

##### Gradle

Add the following configuration into **build.gradle**:

```groovy
repositories {
    jcenter()
}
    
dependencies {
    compile('org.testng:testng:6.10',
            'io.github.sskorol:test-data-supplier:0.7.0'
    )
}
    
test {
    useTestNG() {
        listeners << 'io.github.sskorol.dataprovider.DataProviderTransformer'
    }
}
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-gradle-example) with usage examples.

##### Maven

Add the following configuration into **pom.xml**:

```xml
<dependencies>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>6.10</version>
    </dependency>
    <dependency>
        <groupId>io.github.sskorol</groupId>
        <artifactId>test-data-supplier</artifactId>
        <version>0.7.0</version>
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

##### API

Instead of a common **DataProvider** annotation use one of the following:
 
```java
@DataSupplier
public T getData() {
    //...
}
    
// or
    
@DataSupplier(extractValues = true)
public T getData() {
    //...
}
```

Now you can refer **DataSupplier** the same way as with TestNG **DataProvider**:

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

You can find more examples in a **io.github.sskorol.testcases** package.

#### Limitations

 - no custom names support (method name is used by default);
 - no parallel feature;
 - only ITestContext / Method injections are supported;
 - missing DataProvider warning (affected by TestNG inspections);
 - unused method warning (could be suppressed in IDE):
 
 ![image](https://user-images.githubusercontent.com/6638780/27763889-13dd0b5e-5e95-11e7-8c19-719c6a3a15d9.png)
