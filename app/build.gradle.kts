import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// ВОТ ЗДЕСЬ МЫ УБРАЛИ java.util. И ОСТАВИЛИ ТОЛЬКО Properties()
val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.artspb.moviesearchapp"
    // Исправил синтаксис compileSdk на стандартный
    compileSdk = 36

    defaultConfig {
        applicationId = "com.artspb.moviesearchapp"
        minSdk = 29
        targetSdk = 36 // Желательно чтобы targetSdk и compileSdk совпадали
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Передаем наш спрятанный ключ
        buildConfigField("String", "OMDB_API_KEY", "\"${localProperties.getProperty("OMDB_API_KEY")}\"")
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

    // Включаем генерацию класса BuildConfig
    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // MVVM & LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Библиотека Glide для загрузки изображений
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Retrofit для работы с сетью
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Конвертер Gson для преобразования JSON-ответа сервера в объекты Kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
