plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp) // Necesario para Room
}

android {
    namespace = "com.example.ecomarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecomarket"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    // --- CORRECCIÓN DE COMPATIBILIDAD JVM TARGET (Mantenida) ---
    compileOptions {
        // Establece la compatibilidad de Java en 1.8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        // Establece la compatibilidad de Kotlin en 1.8, haciendo match con Java
        jvmTarget = "1.8"
    }
    // --- FIN CORRECCIÓN DE COMPATIBILIDAD ---
}

dependencies {
    // --- DEPENDENCIAS BASE EXISTENTES ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // --- DEPENDENCIAS AÑADIDAS PARA SOLUCIONAR ERRORES DE REFERENCIA ---

    // 1. AndroidX Room (Base de Datos)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler) // KSP es el procesador de anotaciones

    // 2. AndroidX DataStore (Preferencias Asíncronas)
    implementation(libs.datastore.preferences)

    // 3. Compose Navigation (NavHost, NavController)
    implementation(libs.navigation.compose)

    // 4. Lifecycle (viewModelScope, inyección de ViewModel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    // 5. Gson (Serialización y Deserialización JSON)
    implementation(libs.google.code.gson)
}