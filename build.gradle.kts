import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.apache.opennlp:opennlp-tools:2.1.0")
    implementation("com.mayabot:fastText4j:1.2.3")
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("org.apache.jena:jena-core:4.4.0")
    implementation("org.apache.jena:jena-arq:4.4.0")
    implementation("org.apache.jena:jena-iri:4.4.0")
    implementation(files("libs/transe.jar"))
    implementation("io.github.microutils:kotlin-logging:3.0.4")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.slf4j:slf4j-log4j12:2.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.nd4j:nd4j-native-platform:0.9.1")
    implementation("com.github.haifengl:smile-core:3.0.0")
    implementation("com.github.haifengl:smile-kotlin:3.0.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    application
    java
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("com.github.jk1.dependency-license-report") version "2.1"
    id("com.github.spotbugs") version "5.0.13"
    id("com.diffplug.spotless") version "6.11.0"
    id("org.gretty") version "3.0.3"
//  checkstyle
    war
    jacoco
}

application {
    mainClass.set("com.fujitsu.labs.challenge2019.OpenNLPKt")
}

tasks {
    "wrapper"(Wrapper::class) {
        distributionType = Wrapper.DistributionType.ALL
    }

    withType<Jar> {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass))
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        doLast { println("Finished compiling Kotlin source code") }
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
        doLast { println("Finished compiling Kotlin Test source code") }
    }

    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    compileTestJava {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    jacocoTestReport {
        reports {
            xml.required.set(false)
            csv.required.set(false)
//            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
    }

    withType<JacocoReport> {
        dependsOn("test")
        executionData(withType<Test>())
        classDirectories.setFrom(files(listOf("build/classes/kotlin/main")))
        //  sourceDirectories = files(listOf("src/main/java", "src/main/kotlin"))
        sourceDirectories.setFrom(files(listOf("src/main/java", "src/main/kotlin")))
    }

    dokkaHtml.configure {
        dokkaSourceSets {
            configureEach {
                jdkVersion.set(17)
                noStdlibLink.set(true)
            }
        }
    }

    dokkaJavadoc.configure {
        dokkaSourceSets {
            configureEach {
                jdkVersion.set(11)
                noStdlibLink.set(true)
            }
        }
    }

    test {
        testLogging {
//            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "17"
        reports {
            // observe findings in your browser with structure and code snippets
            html.required.set(true)
            // checkstyle like format mainly for integrations like Jenkins
            xml.required.set(true)
            // similar to the console output, contains issue signature to manually edit baseline files
            txt.required.set(true)
            // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations
            // with Github Code Scanning
            sarif.required.set(true)
        }
    }

    withType<ShadowJar> {
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

detekt {
    source = files("src/main/kotlin")
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    // point to your custom config defining rules to run, overwriting default behavior
    config = files("$projectDir/config/detekt/detekt.yml")
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

gretty {
    servletContainer = "jetty9.4"
}

jacoco {
    toolVersion = "0.8.8"
}

spotbugs {
    ignoreFailures.set(true)
}

spotless {
    java {
        target("src/*/java/**/*.java")
        targetExclude("src/jte-classes/**/*.java", "jte-classes/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()

        // Choose one of these formatters.
        googleJavaFormat("1.15.0") // has its own section below
        formatAnnotations() // fixes formatting of type annotations, see below
    }
}
