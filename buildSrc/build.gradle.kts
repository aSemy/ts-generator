import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  `kotlin-dsl`
  kotlin("jvm") version "1.6.20-M1"
  `project-report`
}


object Versions {
  const val jvmTarget = "11"
  const val kotlinTarget = "1.7"
  const val kotlin = "1.6.20-M1"

  const val kotest = "5.1.0"
}


dependencies {

  implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
  implementation("org.jetbrains.kotlin:kotlin-serialization")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")

  implementation("io.kotest:kotest-framework-multiplatform-plugin-gradle:${Versions.kotest}")
}


tasks.withType<KotlinCompile>().configureEach {

  kotlinOptions {
    jvmTarget = Versions.jvmTarget
    apiVersion = Versions.kotlinTarget
    languageVersion = Versions.kotlinTarget
  }

  kotlinOptions.freeCompilerArgs += listOf(
    "-opt-in=kotlin.RequiresOptIn",
    "-opt-in=kotlin.ExperimentalStdlibApi",
    "-opt-in=kotlin.time.ExperimentalTime",
  )
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(Versions.jvmTarget))
  }

  kotlinDslPluginOptions {
    jvmTarget.set(Versions.jvmTarget)
  }
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}
