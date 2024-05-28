plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.sonarqube") version "3.3" // Aplica el plugin de SonarQube
}

android {
    namespace = "com.task.shopmarket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.task.shopmarket"
        minSdk = 24
        targetSdk = 33
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    google()
    mavenCentral() // Cambiado de jcenter()
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("com.android.volley:volley:1.2.1")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // Bottom Navigation
    implementation("com.google.zxing:core:3.4.1")

    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-auth")

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("de.hdodenhof:circleimageview:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

tasks.withType<AbstractCompile> {
    destinationDirectory.set(file("$buildDir/classes/${name}"))
}

configure<org.sonarqube.gradle.SonarQubeExtension> {
    properties {
        property("sonar.projectKey", "ShopMarket")
        property("sonar.projectName", "ShopMarket")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.login", "sqp_32827696fd9fb87943bffc9a9ab48ea0d27dd103")
        property("sonar.scm.provider", "git") // Definir el proveedor SCM
        // Para desactivar el sensor SCM, usa la siguiente línea:
        // property("sonar.scm.disabled", "true")
    }
}
