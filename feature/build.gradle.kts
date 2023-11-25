val composeVersion: String = "1.4.1"


plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}




android {
    namespace = "com.nishant.library"
    compileSdk = 34
    buildFeatures.compose = true
    defaultConfig {
        //applicationId = "com.nishant.feature"
        minSdk = 21
        targetSdk = 34
        //versionCode = 1
        //versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}

dependencies {

    implementation ("androidx.paging:paging-compose:3.2.1")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0-beta01")

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.compose.ui:ui:$composeVersion")
    implementation ("androidx.compose.material:material:$composeVersion")
    implementation ("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.core:core-ktx:+")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("com.google.dagger:hilt-android:2.44")
    implementation(project(mapOf("path" to ":core")))
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("io.coil-kt:coil-compose:2.4.0")


    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.4.6")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation ("androidx.paging:paging-testing-android:3.3.0-alpha02")



}