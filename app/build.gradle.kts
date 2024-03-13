plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")

//    kotlin("android.extensions")
    kotlin("kapt")

//    id("kotlin-kapt")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}

android {
    namespace = "io.github.bikkysamuel.animetv"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.bikkysamuel.animetv"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    // Retrofit Scalars Converter
    implementation(libs.converter.scalars)


    // Room DB
    implementation(libs.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    // Java HTML Parser
    implementation(libs.jsoup)

    // Glide supports fetching, decoding, and displaying video stills, images, and animated GIFs
    implementation(libs.glide)

    implementation(libs.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.room.compiler)

}