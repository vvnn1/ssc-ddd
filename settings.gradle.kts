rootProject.name = "ssc"
include("ssc-ddd")
include("ssc-ddd:domain")
findProject(":ssc-ddd:domain")?.name = "domain"
include("ssc-ddd:domain:common")
findProject(":ssc-ddd:domain:common")?.name = "common"
include("ssc-ddd:domain:ticket")
findProject(":ssc-ddd:domain:ticket")?.name = "ticket"
