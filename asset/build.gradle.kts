plugins {
    id("java")
    id("java-library")
}

group = "com.github.vvnn1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":common"))
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("jakarta.persistence:jakarta.persistence-api:3.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}