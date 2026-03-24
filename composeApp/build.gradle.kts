import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.googleservices)
    kotlin("plugin.serialization") version "1.9.24"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "inputdata"
            isStatic = true
        }
    }

    js(IR) {
        browser {
            binaries.executable()
        }
    }

    jvm("desktop") {
        compilations["main"].defaultSourceSet {
            kotlin.srcDir("src/desktopMain/kotlin")
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.activity:activity-compose:1.8.2")
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.5.1"))
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(compose.material)
            implementation(compose.materialIconsExtended)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-beta02")
            implementation("cafe.adriel.voyager:voyager-screenmodel:1.1.0-beta02")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:1.1.0-beta02")
            implementation("cafe.adriel.voyager:voyager-transitions:1.1.0-beta02")
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:1.1.0-beta02")
            implementation("com.benasher44:uuid:0.7.0")
            implementation("com.russhwolf:multiplatform-settings:1.1.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            implementation("dev.gitlive:firebase-database:2.1.0")
            implementation("dev.gitlive:firebase-auth:2.1.0")
            implementation("dev.gitlive:firebase-messaging:2.1.0")
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.web.core)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.a2004256_ahmedmohamed.inputdata"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.a2004256_ahmedmohamed.inputdata"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(compose.preview)
    implementation(libs.androidx.activity.ktx)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.a2004256_ahmedmohamed.inputdata.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.a2004256_ahmedmohamed.inputdata"
            packageVersion = "1.0.0"
        }
    }
}