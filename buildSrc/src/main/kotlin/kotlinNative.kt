import org.gradle.api.GradleException
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests

fun KotlinMultiplatformExtension.nativeTarget(
  configure: KotlinNativeTargetWithHostTests.() -> Unit = { }
) {
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  when {
    hostOs == "Mac OS X" -> macosX64("native", configure)
    hostOs == "Linux"    -> linuxX64("native", configure)
    isMingwX64           -> mingwX64("native", configure)
    else                 -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }
}
