plugins {
    id("java")
    id("app.ultradev.hytalegradle") version "2.0.2"
}

group = "com.ludwici.weaponmastery"
version = "1.7.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

hytale {
    version.set("0.7.5")
    allowOp.set(true)
    patchline.set("release")
    includeLocalMods.set(false)
    manifest {
        version.set(project.version.toString())
        serverVersion.set("=0.7.5")
        author("ludwici")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from("src/main/resources")
}

tasks.runServer {
    jvmArgs = jvmArgs + "-XX:+AllowEnhancedClassRedefinition"
}
