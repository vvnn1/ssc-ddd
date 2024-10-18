pluginManagement {
    repositories {
        maven {
            url=uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "ssc"
include("ssc-ddd")
include("ssc-ddd:domain")
findProject(":ssc-ddd:domain")?.name = "domain"
include("ssc-ddd:domain:common")
findProject(":ssc-ddd:domain:common")?.name = "common"
include("ssc-ddd:domain:ticket")
findProject(":ssc-ddd:domain:ticket")?.name = "ticket"
include("ssc-ddd:domain:directory")
findProject(":ssc-ddd:domain:directory")?.name = "directory"
include("ssc-ddd:domain:draft")
findProject(":ssc-ddd:domain:draft")?.name = "draft"
