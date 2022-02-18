
@Suppress("UnstableApiUsage") // centralised repository definitions are incubating
dependencyResolutionManagement {

  repositories {
    mavenCentral()
    jitpack()
    gradlePluginPortal()
  }

  pluginManagement {
    repositories {
      gradlePluginPortal()
      mavenCentral()
      jitpack()
    }
  }
}

fun RepositoryHandler.jitpack() {
  maven("https://jitpack.io")
}
