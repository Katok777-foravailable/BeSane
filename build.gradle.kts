plugins {
    id("java")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

repositories {
    mavenCentral()

    maven {
        name = "paperapi"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "placeholderapi"
        url = uri("https://repo.extendedclip.com/releases/")
    }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

tasks.register<Copy>("jarCopy") {
    from(tasks.jar.get().outputs.files.singleFile)
    into("C:\\Users\\favourite\\Desktop\\servers\\1.16.5\\plugins")
}

tasks.jar {
    finalizedBy("jarCopy")
}