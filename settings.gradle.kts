rootProject.name = "junit-whenalltestsweregreen"
include("sample-projects:junit5-gradle")
findProject(":sample-projects:junit5-gradle")?.name = "junit5-gradle"
