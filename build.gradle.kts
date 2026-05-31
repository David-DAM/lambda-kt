plugins {
    kotlin("jvm") version "2.3.20"
}

group = "com.davinchicoder"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
    implementation("com.amazonaws:aws-lambda-java-events:3.16.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.0")

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Zip>("lambdaZip") {
    from(tasks.jar)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/MANIFEST.MF")

    archiveFileName.set("lambda.zip")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}