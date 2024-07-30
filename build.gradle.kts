import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.ebean") version "13.3.1"
}

group = "com.Keyush"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.5.9"
val junitJupiterVersion = "5.9.1"
val vertxWebVersion = "4.3.8"
val ebeanVersion = "12.12.1"
val queryBeanVersion = "12.12.1"
val mysqlConnectorVersion = "8.0.28"
val hikariCPVersion = "4.0.3"
val logbackVersion = "1.2.11"
val jaxbApiVersion = "2.3.1"

val mainVerticleName = "com.Keyush.WeatherApiVertx.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}


dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-web:$vertxWebVersion")
  implementation("io.vertx:vertx-auth-jwt:$vertxVersion")
  implementation("io.jsonwebtoken:jjwt:0.9.1")
  implementation("io.ebean:ebean:$ebeanVersion") {
    exclude(group = "org.slf4j", module = "slf4j-log4j12")
  }
  implementation("io.ebean:ebean-querybean:$queryBeanVersion")
  implementation("mysql:mysql-connector-java:$mysqlConnectorVersion")
  implementation("com.zaxxer:HikariCP:$hikariCPVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation("org.slf4j:slf4j-api:1.7.32")
  implementation("org.projectlombok:lombok:1.18.22")
  implementation("javax.xml.bind:jaxb-api:$jaxbApiVersion")

  // SLF4J and Logback for logging
  implementation("org.slf4j:slf4j-api:2.0.7")
  implementation("ch.qos.logback:logback-classic:1.4.7")

  // Retrofit and OkHttp dependencies
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:okhttp:4.10.0")

  // Gson for JSON processing
  implementation("com.google.code.gson:gson:2.10.1")

  // Jackson Databind for JSON processing
  implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}


java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
