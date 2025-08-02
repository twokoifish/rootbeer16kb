plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.scottyab.rootbeer.sample"
  compileSdk = 35
  ndkVersion = "27.0.12077973"

  defaultConfig {
    applicationId = "com.scottyab.rootbeer.sample"
    versionName = version.toString()
    versionCode = findProperty("VERSION_CODE").toString().toInt()
    vectorDrawables.useSupportLibrary = true

    base.archivesName = "RootBeerSample-$versionName-[$versionCode]"

    @Suppress("UnstableApiUsage")
    externalNativeBuild {
      cmake {
        arguments += listOf("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
        // added to improve security of binary #180
        cFlags("-fPIC")
        cppFlags("-fPIC")
      }
    }
  }

  buildFeatures {
    viewBinding = true
  }

  // check if the keystore details are defined in gradle.properties (this is so the key is not in github)
  if (findProperty("ROOTBEER_SAMPLE_STORE") != null) {
    signingConfigs {
      // from ~/user/.gradle/gradle.properties
      create("release") {
        storeFile = file(findProperty("ROOTBEER_SAMPLE_STORE").toString())
        keyAlias = findProperty("ROOTBEER_SAMPLE_KEY").toString()
        storePassword = findProperty("ROOTBEER_SAMPLE_STORE_PASS").toString()
        keyPassword = findProperty("ROOTBEER_SAMPLE_KEY_PASS").toString()
      }
    }
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      isMinifyEnabled = false
    }

    release {
      if (rootProject.hasProperty("ROOTBEER_SAMPLE_STORE")) {
        signingConfig = signingConfigs["release"]
      }

      isMinifyEnabled = false
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
  implementation(project(":rootbeerlib"))

  implementation(libs.kotlin.coroutines.core)
  implementation(libs.kotlin.coroutines.android)

  implementation(libs.androidx.core.ktx)

  implementation(libs.androidx.appcompat)
  implementation(libs.android.google.material)

  implementation(libs.nineoldandroids)
  implementation(libs.beerprogressview)

  implementation(libs.timber)
}
