plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
}



android {
    namespace = "com.nishant.core"
    compileSdk = 34

    defaultConfig {
        //applicationId = "com.nishant.core"
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
    }
}



dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.test:core-ktx:1.5.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")

    implementation("androidx.room:room-paging:2.6.0")

    implementation ("androidx.paging:paging-compose:3.2.1")



    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.4.6")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation ("androidx.paging:paging-testing-android:3.3.0-alpha02")

    androidTestImplementation ("androidx.test:runner:1.5.2")

    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation ("androidx.test:rules:1.5.0")



}