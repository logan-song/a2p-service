rootProject.name = "a2p-service"

pluginManagement {
    repositories {
        maven {
            url = uri("http://192.168.2.6:8081/repository/maven-public/")
            credentials {
                username = "admin"
                password = "tour1685!@"
            }
            isAllowInsecureProtocol = true
        }
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("http://192.168.2.6:8081/repository/maven-public/")
            credentials {
                username = "admin"
                password = "tour1685!@"
            }
            isAllowInsecureProtocol = true
        }
    }
}
