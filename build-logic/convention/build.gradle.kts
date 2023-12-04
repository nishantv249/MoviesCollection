
plugins {
    `kotlin-dsl`
}

dependencies{
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.android.gradlePlugin)
}

