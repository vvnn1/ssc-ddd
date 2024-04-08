plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.github.vvnn1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.alibaba:druid-spring-boot-starter:1.1.10")
    implementation("mysql:mysql-connector-java:8.0.21")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("junit:junit")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}