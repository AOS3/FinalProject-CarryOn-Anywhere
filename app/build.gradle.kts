alias(libs.plugins.android.application)
alias(libs.plugins.kotlin.android)
alias(libs.plugins.kotlin.compose)
    // firebase

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
id("com.google.gms.google-services")
}

@@ -13,7 +15,7 @@ android {
defaultConfig {
applicationId = "com.lion.FinalProject_CarryOn_Anywhere"
minSdk = 24
        targetSdk = 35
        targetSdk = 34
versionCode = 1
versionName = "1.0"

@@ -58,11 +60,23 @@ dependencies {
androidTestImplementation(libs.androidx.ui.test.junit4)
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)

implementation("androidx.compose.material:material-icons-extended:1.7.6")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
implementation("com.google.firebase:firebase-analytics")
implementation("com.google.firebase:firebase-firestore:25.1.1")
implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
}

kapt {
    correctErrorTypes = true
}