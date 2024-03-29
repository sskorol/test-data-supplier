import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    `java-library`
    jacoco
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0-rc-1"
    id("org.sonarqube") version "4.4.1.3373"
    id("net.researchgate.release") version "3.0.2"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "io.github.sskorol"
version = version
description = "Test Data Supplier is an extended version of common TestNG DataProvider."

val gradleScriptDir: String by extra("${rootProject.projectDir}/gradle")
val projectUrl by extra("https://github.com/sskorol/test-data-supplier")
val moduleName by extra("io.github.sskorol.testdatasupplier")

val aspectjVersion by extra("1.9.19")
val jacksonVersion by extra("2.15.2")
val lombokVersion by extra("1.18.28")
val poiVersion by extra("5.2.3")
val joorVersion by extra ("0.9.14")
val testngVersion by extra("7.8.0")
val streamexVersion by extra("0.8.1")
// Don't update to the latest, as it's outdated
val vavrVersion by extra("0.10.4")
val reflectionsVersion by extra("0.10.2")
val commonsCsvVersion by extra("1.10.0")
val gsonVersion by extra("2.10.1")
val assertjVersion by extra("3.24.2")
val logbackVersion by extra("1.4.8")
val log4jVersion by extra("2.20.0")
val mockitoVersion by extra("5.4.0")

val agent: Configuration by configurations.creating

// Excluding shadowed jars from pom.xml https://github.com/gradle/gradle/issues/10861#issuecomment-576562961
val internal by configurations.creating {
    isVisible = false
    isCanBeConsumed = false
    isCanBeResolved = false
}

configurations {
    compileClasspath.get().extendsFrom(internal)
    runtimeClasspath.get().extendsFrom(internal)
    testCompileClasspath.get().extendsFrom(internal)
    testRuntimeClasspath.get().extendsFrom(internal)
    create("spiOff").apply {
        extendsFrom(configurations.compileOnly.get())
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    agent("org.aspectj:aspectjweaver:${aspectjVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    api("org.jooq:joor:${joorVersion}")
    api("org.testng:testng:${testngVersion}")
    api("one.util:streamex:${streamexVersion}")
    api("io.vavr:vavr:${vavrVersion}")
    api("org.aspectj:aspectjrt:${aspectjVersion}")
    api("org.reflections:reflections:${reflectionsVersion}")
    api("org.apache.commons:commons-csv:${commonsCsvVersion}")
    api("com.google.code.gson:gson:${gsonVersion}")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
    api("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    api("org.apache.poi:poi:${poiVersion}")
    api("org.apache.poi:poi-ooxml:${poiVersion}")
    api("org.assertj:assertj-core:${assertjVersion}")
    // Transitive dependency: <=1.33 version has vulnerabilities. Remove when updated by top-level packages.
    api("org.yaml:snakeyaml:2.2")
    testImplementation("ch.qos.logback:logback-classic:${logbackVersion}")
    testImplementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    testImplementation("org.mockito:mockito-core:${mockitoVersion}")
}

jacoco.toolVersion = "0.8.8"
tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${buildDir}/reports/jacoco/jacoco.xml"))
        html.outputLocation.set(file("${buildDir}/reports/jacoco"))
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "io/github/sskorol/core/**/*Aspect.class",
                    "io/github/sskorol/core/**/*Interceptor.class"
                )
            }
        })
    )
}

sonar {
    properties {
        properties(
            hashMapOf<String, String>(
                "sonar.host.url" to (System.getenv("SONAR_URL") ?: ""),
                "sonar.login" to (System.getenv("SONAR_TOKEN") ?: ""),
                "sonar.projectKey" to "io.github.sskorol:test-data-supplier",
                "sonar.projectName" to "Test Data Supplier",
                "sonar.organization" to "sskorol-github",
                "sonar.qualitygate.wait" to "true",
                "sonar.sourceEncoding" to "UTF-8",
                "sonar.coverage.exclusions" to "**/*Aspect.*,**/*Interceptor.*",
                "sonar.coverage.jacoco.xmlReportPaths" to "build/reports/jacoco/jacoco.xml"
            )
        )
    }
}

project.tasks["sonar"].dependsOn("jacocoTestReport")

tasks.withType<JacocoReport> {
    dependsOn("build")
}

tasks.withType(Wrapper::class) {
    gradleVersion = "8.2.1"
}

tasks.compileJava {
    inputs.property("moduleName", moduleName)
    doFirst {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(
            listOf(
                "--module-path", classpath.asPath,
                "-Xlint:deprecation",
                "-Xlint:unchecked"
            )
        )
        classpath = files()
    }
}

tasks.compileTestJava {
    inputs.property("moduleName", moduleName)
    doFirst {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(
            listOf(
                "--module-path", classpath.asPath,
                "--patch-module", "$moduleName=" + files(sourceSets.test.map { it.java.srcDirs }).asPath,
                "-Xlint:deprecation",
                "-Xlint:unchecked"
            )
        )
        classpath = files()
    }
}

tasks.test {
    val modulePrefix = "io.github.sskorol.testdatasupplier/io.github.sskorol"
    val javaUtilModule = "java.base/java.util"
    val suiteName = System.getenv("TEST_SUITE") ?: "smoke"

    testLogging {
        events.addAll(listOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED))
        exceptionFormat = TestExceptionFormat.FULL
    }

    useTestNG {
        suites("src/test/resources/${suiteName}-suite.xml")
    }

    inputs.property("moduleName", moduleName)
    doFirst {
        val args = mutableListOf(
            "-javaagent:${agent.singleFile}",
            "--module-path", classpath.asPath,
            "--add-modules", "ALL-MODULE-PATH",
            "--add-opens", "${modulePrefix}.testcases=org.testng",
            "--add-opens", "${modulePrefix}.listeners=org.testng",
            "--add-opens", "${modulePrefix}.datasuppliers=org.testng",
            "--add-opens", "${modulePrefix}.testcases=org.jooq.joor",
            "--add-opens", "${modulePrefix}.entities=org.jooq.joor",
            "--add-opens", "${modulePrefix}.datasuppliers=org.jooq.joor",
            "--add-opens", "${modulePrefix}.data=org.jooq.joor",
            "--add-opens", "${modulePrefix}.entities=com.google.gson",
            "--add-opens", "${modulePrefix}.entities=com.fasterxml.jackson.databind",
            "--add-opens", "${javaUtilModule}=one.util.streamex",
            "--add-opens", "${javaUtilModule}.stream=one.util.streamex",
            "--patch-module", "$moduleName=" + files(
                sourceSets.test.map { it.java.classesDirectory },
                sourceSets.test.map { it.output.resourcesDir!!.absoluteFile }
            ).asPath
        )
        val shouldDebug = System.getenv("DEBUG")?.toBoolean() ?: false
        if (shouldDebug) {
            args.add("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=*:5005")
        }
        jvmArgs = args
        classpath = files()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            suppressAllPomMetadataWarnings()
            versionMapping {
                allVariants {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/sskorol/test-data-supplier")
                from(components["java"])
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("sskorol")
                        name.set("Serhii Korol")
                        email.set("serhii.s.korol@gmail.com")
                    }
                }
                scm {
                    developerConnection.set("scm:git:git://github.com/sskorol/test-data-supplier")
                    connection.set("scm:git:git://github.com/sskorol/test-data-supplier")
                    url.set("https://github.com/sskorol/test-data-supplier")
                }
                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/sskorol/test-data-supplier/issues")
                }
            }
        }
    }
}

nexusPublishing {
    this.repositories {
        sonatype {
            val osshUsername = System.getenv("OSSH_USERNAME") ?: ""
            val osshPassword = System.getenv("OSSH_PASSWORD") ?: ""
            username.set(osshUsername)
            password.set(osshPassword)
        }
    }
}

signing {
    val signingKeyId: String = System.getenv("SIGNING_KEY_ID") ?: ""
    val signingKey: String = System.getenv("SIGNING_KEY") ?: ""
    val signingPassword: String = System.getenv("SIGNING_PASSWORD") ?: ""
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["maven"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { !project.version.toString().endsWith("-SNAPSHOT") }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

tasks.jar {
    inputs.property("moduleName", moduleName)
    manifest {
        attributes(
            mapOf(
                "Automatic-Module-Name" to moduleName,
                "Specification-Title" to project.name,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
    from(sourceSets.main.map { it.output })
    from("src/main/services") {
        into("META-INF/services")
    }
}

tasks.register<Jar>("sourceJar") {
    group = "sourceJar"
    description = "Build a jar from sources"
    dependsOn(tasks.classes)
    inputs.property("moduleName", moduleName)
    manifest {
        attributes(
            mapOf(
                "Automatic-Module-Name" to moduleName,
                "Specification-Title" to project.name,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
    archiveClassifier.set("sources")
    from(sourceSets.main.map { it.output })
    from("src/main/services") {
        into("META-INF/services")
    }
}

tasks.register<Jar>("spiOffJar") {
    group = "spiOffJar"
    description = "Build a jar from sources excluding SPI"
    dependsOn(tasks.classes)
    inputs.property("moduleName", moduleName)
    manifest {
        attributes(
            mapOf(
                "Automatic-Module-Name" to moduleName,
                "Specification-Title" to project.name,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
    archiveClassifier.set("spi-off")
    from(sourceSets.main.map { it.output })
}

tasks.withType(Javadoc::class) {
    inputs.property("moduleName", moduleName)
    doFirst {
        options.modulePath = classpath.files.toList()
    }
    source = sourceSets.main.get().allJava
    val opt = (options as StandardJavadocDocletOptions)
    opt.addStringOption("Xdoclint:none", "-quiet")
    opt.addBooleanOption("html5", true)
}

tasks.withType(Sign::class) {
    dependsOn("sourceJar")
}

artifacts {
    add("archives", tasks.getByName("sourceJar"))
    add("archives", tasks.getByName("spiOffJar"))
    add("archives", tasks.getByName("javadocJar"))
    add("spiOff", tasks.getByName("spiOffJar"))
}
