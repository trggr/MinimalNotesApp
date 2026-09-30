plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget()

    jvm("jvm")
    
    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

android {
    namespace = "com.example.notes.shared"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}

tasks.register<JavaExec>("runCli") {
    group = "application"
    description = "Runs the notes CLI application on the JVM"
    
    val jvmCompilation = kotlin.targets.getByName("jvm").compilations.getByName("main")
    
    // Safely combine dependency files and output classes dirs
    classpath = files(jvmCompilation.runtimeDependencyFiles, jvmCompilation.output.classesDirs)
    
    mainClass.set("com.example.notes.CliAppKt")
}

