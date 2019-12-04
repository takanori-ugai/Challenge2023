import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

repositories {
  jcenter()
  mavenCentral()
}

val ktlintCfg by configurations.creating

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("org.apache.opennlp:opennlp-tools:1.9.1")
  implementation("com.mayabot:fastText4j:1.2.3")
  implementation("com.opencsv:opencsv:5.0")
  implementation("org.apache.jena:jena-core:3.13.1")
  implementation("org.apache.jena:jena-arq:3.13.1")
  implementation("org.apache.jena:jena-iri:3.13.1")
  implementation(files("libs/transe.jar"))
  implementation("io.github.microutils:kotlin-logging:1.7.7")
  implementation("org.slf4j:slf4j-api:1.7.29")
  implementation("org.slf4j:slf4j-log4j12:1.7.29")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
  implementation("org.nd4j:nd4j-native-platform:0.9.1")
  implementation("com.github.haifengl:smile-core:1.5.3")
  testImplementation("junit:junit:4.12")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  ktlintCfg("com.pinterest:ktlint:0.35.0")
}

plugins {
  kotlin("jvm") version "1.3.60"
  application
  java
  id("org.jetbrains.dokka") version "0.10.0"
  id("io.gitlab.arturbosch.detekt") version "1.1.1"
  id("com.github.sherter.google-java-format") version "0.8"
  id("com.github.johnrengelman.shadow") version "5.2.0"
  id("org.gretty") version "2.3.1"
//  checkstyle
  war
  jacoco
}

application {
  mainClassName = "com.fujitsu.labs.challenge2019.OpenNLPKt"
}

tasks {
  "wrapper"(Wrapper::class) {
    distributionType = Wrapper.DistributionType.ALL
  }

  withType<Jar> {
    manifest {
      attributes(mapOf("Main-Class" to application.mainClassName))
    }
  }

}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
  doLast { println("Finished compiling Kotlin source code") }
}

val ktlint by tasks.creating(JavaExec::class) {
  group = "verification"
  main = "com.pinterest.ktlint.Main"
  classpath = ktlintCfg
  args("--verbose", "--reporter=plain", "--reporter=checkstyle,output=$buildDir/reports/ktlint.xml", "src/main/kotlin/**/*.kt")

}


val ktlintFormat by tasks.creating(JavaExec::class) {
  group = "formatting"
  main = "com.pinterest.ktlint.Main"
  classpath = ktlintCfg
  args("-F", "src/**/*.kt")

}


val dokka by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
  outputFormat = "javadoc"
  outputDirectory = "$buildDir/javadoc"
  configuration {
    jdkVersion = 8
    noStdlibLink = true
  }
}

//val detekt by tasks.getting(io.gitlab.arturbosch.detekt.Detekt::class) {
detekt {
  input = files("src/main/kotlin")
  config = files("config.yml")
}

gretty {
  servletContainer = "jetty9.4"
}

tasks.getByName<Test>("test") {
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    showStandardStreams = true
  }
}

jacoco {
  toolVersion = "0.8.4"
}

tasks.jacocoTestReport {
  reports {
    html.isEnabled = true
    xml.isEnabled = true
  }
}

tasks.withType<JacocoReport> {
  dependsOn("test")
  executionData(tasks.withType<Test>())
//  setClassDirectories(files(listOf("build/classes/kotlin/main")))
  classDirectories.setFrom(files(listOf("build/classes/kotlin/main")))
  sourceDirectories.setFrom(files(listOf("src/main/java", "src/main/kotlin")))
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

