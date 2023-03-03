# Test Data Supplier 

[![Build Status](https://github.com/sskorol/test-data-supplier/actions/workflows/ci.yml/badge.svg?branch=main&event=push)](https://github.com/sskorol/test-data-supplier/actions/workflows/ci.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.sskorol%3Atest-data-supplier)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=coverage)](https://sonarcloud.io/component_measures?id=io.github.sskorol%3Atest-data-supplier&metric=coverage)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=bugs)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.sskorol%3Atest-data-supplier&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=io.github.sskorol%3Atest-data-supplier)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.github.sskorol/test-data-supplier)
[![GitHub license](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://goo.gl/9GLmMZ)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/sskorol/test-data-supplier.svg?style=social)](https://twitter.com/intent/tweet?text=Check%20new%20Test%20Data%20Supplier%20library:&url=https://github.com/sskorol/test-data-supplier)

## Table of Contents

- [Description](#description)
- [Supported Flags](#supported-flags)
- [Supported Return Types](#supported-return-types)
- [Usage](#usage)
  - [Gradle - Java < 9](#gradle---java--9)
  - [Maven - Java < 9](#maven---java--9)
  - [Gradle - Java 11-16 w/o modules](#gradle---java-11-16-wo-modules)
  - [Gradle - Java 11-16 w/ modules](#gradle---java-11-16-w-modules)
  - [Gradle - Java 17+ w/o modules](#gradle---java-17-wo-modules)
  - [Maven - Java 17+ w/o modules](#maven---java-17-wo-modules)
  - [API](#api)
  - [JSON, CSV, YAML and XLSX processors](#json-csv-yaml-and-xlsx-processors)
  - [DB support](#db-support)
  - [Factory](#factory)
  - [Tracking meta-data](#tracking-meta-data)
  - [IAnnotationTransformer restriction](#iannotationtransformer-restriction)
- [IntelliJ IDEA support](#intellij-idea-support) 

## Description

This repository contains TestNG **DataProvider** wrapper (the latest version is based on TestNG 7.7.1) which helps to supply test data in a more flexible way.

Common **DataProvider** forces using quite old and ugly syntax which expects one of the following types to be returned from DP method's body:

 - Object[][]
 - Iterator<Object[]>

That's weird as developers tend to use `Stream` and `Collection` API for data manipulation in the modern Java world.

Just imagine if you could use the following syntax to supply some filtered and sorted data into test method's signature:

```java
@DataSupplier
public Stream<User> getData() {
    return Stream
        .of(
            new User("Max", "password2"),
            new User("Black Fox", "password3"),
            new User("Mark", "password1")
        )
        .filter(u -> !u.getName().contains("Fox"))
        .sorted(comparing(User::getPassword));
}
    
@Test(dataProvider = "getData")
public void shouldSupplyStreamData(final User user) {
    // ...
}
```

Much better and flexible than two-dimensional arrays or iterators, isn't it?

And what if we don't want to iterate the same test N times depending on collection size? What if we want to inject it into test's signature like the following?

```java
@DataSupplier(transpose = true)
public List<User> getTransposedData() {
    return StreamEx
        .of(
            new User("username1", "password1"),
            new User("username2", "password2")
        )
        .toList();
}
        
@Test(dataProvider = "getTransposedData")
public void shouldSupplyExtractedListData(final List<User> users) {
    // ...
}
```

Or if you want to extract the values of your collection and inject into test's signature, you can combine `transpose` with a `flatMap`:

```java
@DataSupplier(transpose = true, flatMap = true)
public Set<User> getExtractedData() {
    return StreamEx.of("product1", "product2", "product1").toSet();
}
        
@Test(dataProvider = "getExtractedData")
public void shouldSupplyExtractedListData(final String... products) {
    // ...
}
```

Java-like **flatMap** operation can be applied even to more complicated structures like `Map` to extract values for each row:

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

[**Go top**](#test-data-supplier) :point_up:

## Supported flags

- **name**: sets a custom name for `DataSupplier` (method name is used by default)
- **transpose**: translates data column into a single row
- **flatMap**: behaves pretty much like a native Java Stream operation
- **runInParallel**: executes each data-driven test in parallel rather than sequentially
- **indices**: filters the underlying collection by given indices
- **propagateTestFailure**: fails the test in case of `DataSupplier` failure (skips by default)

[**Go top**](#test-data-supplier) :point_up:

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

[**Go top**](#test-data-supplier) :point_up:

## Usage

### Gradle - Java < 9

Add the following configuration into **build.gradle**:

```groovy
repositories {
    mavenCentral()
}
    
configurations {
    agent
}

sourceCompatibility = JavaVersion.VERSION_1_8

ext {
    aspectjVersion = '1.9.7'
}

[compileJava, compileTestJava]*.options*.compilerArgs = ['-parameters']
    
dependencies {
    agent "org.aspectj:aspectjweaver:${aspectjVersion}"
    implementation(
            "org.aspectj:aspectjweaver:${aspectjVersion}",
            'org.testng:testng:6.14.3',
            'io.github.sskorol:test-data-supplier:1.7.0'
    )
}
    
test {
    doFirst {
        jvmArgs("-javaagent:${configurations.agent.singleFile}")
    }
    
    useTestNG()
}
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-gradle-example) with usage examples.

[**Go top**](#test-data-supplier) :point_up:

### Maven - Java < 9

Add the following configuration into **pom.xml**:

```xml
<properties>
    <aspectj.version>1.9.7</aspectj.version>
    <java.version>1.8</java.version>
    <compiler.plugin.version>3.8.0</compiler.plugin.version>
    <surefire.plugin.version>2.20.1</surefire.plugin.version>
</properties>
    
<dependencies>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>${aspectj.version}</version>
    </dependency>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>6.14.3</version>
    </dependency>
    <dependency>
        <groupId>io.github.sskorol</groupId>
        <artifactId>test-data-supplier</artifactId>
        <version>1.7.0</version>
    </dependency>
</dependencies>
    
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${compiler.plugin.version}</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
                <compilerArgs>
                    <arg>-parameters</arg>
                </compilerArgs>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>${surefire.plugin.version}</version>
            <configuration>
                <argLine>
                    -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
                </argLine>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Check a separate [project](https://github.com/sskorol/test-data-supplier-maven-example) with usage examples.

[**Go top**](#test-data-supplier) :point_up:

### Gradle - Java 11-16 w/o modules

```groovy
plugins {
    id 'java'
}
    
sourceCompatibility = JavaVersion.VERSION_11
    
repositories {
    mavenCentral()
}
    
configurations {
    agent
}

ext {
    aspectjVersion = '1.9.7'
}

[compileJava, compileTestJava]*.options*.compilerArgs = ['-parameters']
    
dependencies {
    agent "org.aspectj:aspectjweaver:${aspetjVersion}"
    implementation(
            "org.aspectj:aspectjweaver:${aspectjVersion}",
            'org.testng:testng:7.4.0',
            'io.github.sskorol:test-data-supplier:1.9.7'
    )
}
    
test {
    doFirst {
        jvmArgs("-javaagent:${configurations.agent.singleFile}")
    }
    
    useTestNG()
}
```

[**Go top**](#test-data-supplier) :point_up:

### Gradle - Java 11-16 w/ modules

It's a bit tricky in terms of building and testing modular applications:

```groovy
plugins {
    id 'java-library'
    id 'java'
}
    
ext {
    moduleName = 'your.module.name'
}
    
sourceCompatibility = JavaVersion.VERSION_11
    
repositories {
    mavenCentral()
}
    
configurations {
    agent
}

ext {
    aspectjVersion = '1.9.7'
}
    
dependencies {
    agent "org.aspectj:aspectjweaver:${aspectjVersion}"
    implementation(
            "org.aspectj:aspectjweaver:${aspectjVersion}",
            'org.testng:testng:7.4.0',
            'io.github.sskorol:test-data-supplier:1.9.7'
    )
}
    
compileJava {
    inputs.property("moduleName", moduleName)
    doFirst {
        options.compilerArgs = [
                '--module-path', classpath.asPath
        ]
        classpath = files()
    }
}
   
compileTestJava {
    inputs.property("moduleName", moduleName)
    doFirst {
        options.compilerArgs = [
                '--module-path', classpath.asPath,
                '--patch-module', "$moduleName=" + files(sourceSets.test.java.srcDirs).asPath,
        ]
        classpath = files()
    }
}
   
test {
    useTestNG()
   
    inputs.property("moduleName", moduleName)
    doFirst {
        jvmArgs = [
                "-javaagent:${configurations.agent.singleFile}",
                '--module-path', classpath.asPath,
                '--add-modules', 'ALL-MODULE-PATH',
                '--add-opens', 'your.module.name/test.package.path=org.testng',
                '--add-opens', 'your.module.name/test.package.path=org.jooq.joor',
                '--patch-module', "$moduleName=" + files(sourceSets.test.java.outputDir).asPath
        ]
        classpath = files()
    }
}
```

Your **module-info.java** may look like the following:

```java
module your.module.name {
    requires io.github.sskorol.testdatasupplier;
    requires org.testng;
   
    // Optional
    provides io.github.sskorol.core.IAnnotationTransformerInterceptor
        with path.to.transformer.ImplementationClass;
   
    provides io.github.sskorol.core.DataSupplierInterceptor
        with path.to.interceptor.ImplementationClass;
}
```

[**Go top**](#test-data-supplier) :point_up:

### Gradle - Java 17+ w/o modules

Note that `test-data-supplier:2.0.0+` has been compiled with java 17. It means you must use the same language level in your build file.

```groovy
plugins {
    id 'java'
}
    
sourceCompatibility = JavaVersion.VERSION_17
    
repositories {
    mavenCentral()
}
    
configurations {
    agent
}

ext {
    aspectjVersion = '1.9.19'
}

[compileJava, compileTestJava]*.options*.compilerArgs = ['-parameters']
    
dependencies {
    agent "org.aspectj:aspectjweaver:${aspectjVersion}"
    implementation(
            "org.aspectj:aspectjweaver:${aspectjVersion}",
            'org.testng:testng:7.7.1',
            'io.github.sskorol:test-data-supplier:2.2.0'
    )
}
    
test {
    doFirst {
        jvmArgs(
            "-javaagent:${configurations.agent.singleFile}",
            '--add-opens', 'java.base/java.lang=ALL-UNNAMED'
        )
    }
    
    useTestNG()
}
```

[**Go top**](#test-data-supplier) :point_up:

### Maven - Java 17+ w/o modules

```xml
<properties>
    <aspectj.version>1.9.19</aspectj.version>
    <java.version>17</java.version>
    <compiler.plugin.version>3.11.0</compiler.plugin.version>
    <surefire.plugin.version>3.0.0-M9</surefire.plugin.version>
</properties>
    
<dependencies>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>${aspectj.version}</version>
    </dependency>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.7.1</version>
    </dependency>
    <dependency>
        <groupId>io.github.sskorol</groupId>
        <artifactId>test-data-supplier</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>
    
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${compiler.plugin.version}</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
                <compilerArgs>
                    <arg>-parameters</arg>
                </compilerArgs>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>${surefire.plugin.version}</version>
            <configuration>
                <argLine>
                    -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
                </argLine>
            </configuration>
        </plugin>
    </plugins>
</build>
```

[**Go top**](#test-data-supplier) :point_up:

### API

Instead of a common **DataProvider** annotation use the following:
 
```java
@DataSupplier
public T getData() {
    //...
}
```

**DataSupplier** supports the following args: **name**, **transpose**, **flatMap**, **runInParallel**, **indices** and **propagateTestFailure**. 

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

[**Go top**](#test-data-supplier) :point_up:

### JSON, CSV, YAML and XLSX processors

Test data supplier supports JSON, CSV, YML and XLSX data retrieval. Assuming you have the following resources:

```csv
username,password
"admin","admin"
"sskorol","password"
"guest","123"
```

```json
[
  {
    "username": "admin",
    "password": "admin"
  },
  {
    "username": "sskorol",
    "password": "password"
  },
  {
    "username": "guest",
    "password": "123"
  }
]
```

```yaml
---
 username: admin
 password: admin
---
 username: sskorol
 password: password
---
 username: guest
 password: '123'
```

```xlsx
USERNAME  PASSWORD
admin     admin
sskorol   password
guest     123
```

You can now map Java entities to these data sources using **@Source** annotation, which accepts either local file name 
or URL:

```java
@Data
@Source(path = "users.csv")
public class User {
    @FieldName("username")
    private final String name;
    private final String password;
}
```

```java
@Data
@Source(path = "users.json")
public class User {
    @SerializedName("username")
    private final String name;
    private final String password;
}
```

```java
@Data
@NoArgsConstructor
@Source(path = "users.yml")
public class User {
    @JsonProperty("username")
    private final String name;
    private final String password;
}
```

```java
@Data
@NoArgsConstructor
@Source(path = "users.xlsx")
@Sheet(name = "sheet_1")
public class User {
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;
}
```

In case if some Java field's name differs from its data source representation, you can assign a valid name via 
**@FieldName** for CSV, **@SerializedName** for JSON and **@JsonProperty** for YML data type.

Excel support is experimental. 2.0.0 version used [ZeroCell](https://github.com/creditdatamw/zerocell) library based on [Apache POI](https://github.com/apache/poi) to simplify corresponding files processing.
Since 2.1.0, there's a custom implementation with similar approach, but minor improvements, e.g. there's no need to use column index anymore.

In terms of fields' mapping, you can use custom `@Column` annotation (don't confuse with ZeroCell Column).
You should also make sure you provided a sheet name via corresponding `@Sheet` annotation. Otherwise, the first one will be used.

Similarly to ZeroCell, you can use either default or custom fields' converters. Here's a list of defaults:

- BooleanConverter
- DoubleConverter
- IntegerConverter
- LocalDateConverter
- LocalDateTimeConverter
- StringConverter

To use custom converter, you should specify its class via `@Column` annotation.

```java
@Column(name = "Tags", converter = StringToListConverter.class)
private List<String> data;
```

And the actual implementation may look like the following:

```java
public class StringToListIConverter extends DefaultConverter<List<String>> {
    @Override
    public List<String> convert(final String value) {
        return asList(value.split(","));
    }
}
```

Custom converters must extend `DefaultConverter` class.
Also note that by default `test-data-supplier` uses an implicit conversion based on the field type.
So you don't have to explicitly specify a converter if it's among the defaults.

Local data sources must be located in a classpath. You usually use **resources** folder for that.

Then, within `@DataSupplier` you can call a special **TestDataReader** builder to retrieve data from CSV, JSON, YML or XLSX data source. 
See javadocs to get more details.

```java
@DataSupplier
public StreamEx<User> getUsers() {
    return use(CsvReader.class).withTarget(User.class).withSource("users.csv").read();
}
```

```java
@DataSupplier
public StreamEx<User> getUsers() {
    return use(JsonReader.class).withTarget(User.class).withSource("http://users.json").read();
}
```

```java
@DataSupplier
public StreamEx<User> getUsers() {
    return use(YamlReader.class).withTarget(User.class).read();
}
```

```java
@DataSupplier
public StreamEx<User> getUsers() {
    return use(XlsxReader.class).withTarget(User.class).read();
}
```

If you want to specify a custom source in runtime, you can remove **@Source** annotation and use **withSource** builder 
method instead.

Note that in case of a data reading error or any kind of exception thrown in a `@DataSupplier` body,
the corresponding test will be skipped. That's a default TestNG behaviour.
However, you can set `propagateTestFailure` flag (introduced in TestNG 7.6.0) to explicitly mark the test as failed.

[**Go top**](#test-data-supplier) :point_up:

### DB support

Technically, there's no need to create an additional ORM wrapper to work with databases. But it's worth to show how to perform such integration.

Let's use [ebean](https://ebean.io/docs/getting-started/) and Postgres as an example.

First, create an `application-test.yaml` with db connection details in your test resources' folder:
```yaml
ebean:
  test:
    useDocker: false
    platform: postgres
    ddlMode: none
    dbName: your_db
    dbSchema: your_schema
    postgres:
      username: your_username
      password: your_password
      url: jdbc:postgresql://localhost:5432/your_db
```

Next, create a mapping with your DB table:
```java
@MappedSuperclass
public class BaseEntity extends Model {
    @Id
    long id;
}

@Entity
@Table(name = "testing.users")
public class UserEntity extends BaseEntity {
    @NotNull
    public String email;

    @Column(name = "is_active")
    public boolean isActive;
}
```

Now, you can supply users to your test the following way:
```java
@DataSupplier
public List<UserEntity> usersData() {
    return find(UserEntity.class)
        .where()
        .like("email", "%korol%@gmail.com")
        .and()
        .eq("is_active", true)
        .findList();
}
```

[**Go top**](#test-data-supplier) :point_up:

### Factory

You can specify **DataSupplier** for **Factory** annotation as well as for common test methods.

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

[**Go top**](#test-data-supplier) :point_up:

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

[**Go top**](#test-data-supplier) :point_up:

### IAnnotationTransformer restriction

TestNG restricts users in a number of **IAnnotationTransformer** implementations. You may have not more than a single transformer within project's scope.
As **test-data-supplier** uses this interface for its internal stuff, you won't be able to apply your own implementation.

In case if you still need to add a custom **IAnnotationTransformer**, you have to implement the following interface: 

```java
public class IAnnotationTransformerInterceptorImpl implements IAnnotationTransformerInterceptor {

    @Override
    public void transform(IFactoryAnnotation annotation, Method testMethod) {
    }

    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    }

    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {
    }

    @Override
    public void transform(IListenersAnnotation annotation, Class testClass) {
    }
}
```

It's just an SPI wrapper for common TestNG feature. Use the same technique as for **DataSupplierInterceptor** to include it into your project.

Note that in case if you want to manage **DataProviderTransformer** manually, you have to use a special spi-off distribution:

```groovy
dependencies {
    implementation 'io.github.sskorol:test-data-supplier:2.2.0:spi-off'
}
```

[**Go top**](#test-data-supplier) :point_up:

## IntelliJ IDEA support

**Test Data Supplier** also has an IntelliJ IDEA plugin. Just install **test-data-supplier-plugin** from the official JetBrains repository.

More information about its features can be found on the related [GitHub](https://github.com/sskorol/test-data-supplier-plugin) page.

[**Go top**](#test-data-supplier) :point_up:
