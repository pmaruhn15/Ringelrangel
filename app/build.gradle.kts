plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ringelrangel.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ringelrangel.app"
        minSdk = 26
        targetSdk = 34

        val versionPropsFile = rootProject.file("version.properties")
        val versionProps = java.util.Properties()
        if (versionPropsFile.exists()) {
            versionProps.load(versionPropsFile.inputStream())
        }
        val major = (versionProps["VERSION_MAJOR"] as? String)?.toIntOrNull() ?: 1
        val minor = (versionProps["VERSION_MINOR"] as? String)?.toIntOrNull() ?: 0
        val patch = (versionProps["VERSION_PATCH"] as? String)?.toIntOrNull() ?: 0
        val build = (versionProps["VERSION_BUILD"] as? String)?.toIntOrNull() ?: 1

        versionCode = major * 10000 + minor * 100 + patch
        versionName = "$major.$minor.$patch"

        buildConfigField("int", "VERSION_BUILD", "$build")
        buildConfigField("String", "GITHUB_REPO", "\"pmaruhn15/Ringelrangel\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // OkHttp for GitHub API calls (update check)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JSON parsing
    implementation("org.json:json:20231013")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
