rootProject.name = "ssc-ddd"
include("common")
include("domain")
include("domain:asset")
findProject(":domain:asset")?.name = "asset"
include("domain:ticket")
findProject(":domain:ticket")?.name = "ticket"
include("domain:common")
findProject(":domain:common")?.name = "common"
