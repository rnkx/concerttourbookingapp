plugins {
    id("com.android.application")
    id("com.google.gms.google-services")  // Add this line
}

android {
    namespace = "com.example.a6012cem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.a6012cem"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation ("com.google.firebase:firebase-firestore: 24.9.1")
    implementation ("com.google.firebase:firebase-bom:32.7.0")
    implementation ("com.google.android.gms:play-services-auth:20.0.0")
    // Firebase Analytics (optional)
    implementation ("com.google.firebase:firebase-analytics")

    // Add this for better network handling
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Firebase Realtime Database
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-storage") // Add this
    implementation ("com.google.code.gson:gson:2.10.1")


}