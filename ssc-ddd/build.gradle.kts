plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven {
            url=uri("https://maven.aliyun.com/nexus/content/groups/public")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ssc-ddd:domain:common"))
    implementation(project(":ssc-ddd:domain:directory"))
    implementation(project(":ssc-ddd:domain:ticket"))
    implementation(project(":ssc-ddd:domain:draft"))
    implementation(project(":ssc-ddd:domain:resource"))
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("com.h2database:h2:2.3.232")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}