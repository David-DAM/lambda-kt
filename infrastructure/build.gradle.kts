plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:2.220.0")
    implementation("software.constructs:constructs:10.4.2")
}

application {
    mainClass.set("com.davinchicoder.InfraKt")
}

kotlin {
    jvmToolchain(21)
}