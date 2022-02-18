/*
 * Copyright 2017 Alicia Boya García
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    kotlin("multiplatform")
    id("io.kotest.multiplatform")
    `maven-publish`
    id("me.qoomon.git-versioning") version "5.1.2"
//    jacoco // not easy to set up with KMP - migrate to Kover?
}

project.group = "me.ntrrgc"
project.version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
    refs {
        branch(".+") { version = "\${ref}-SNAPSHOT" }
        tag("v(?<version>.*)") { version = "\${ref.version}" }
    }

    // optional fallback configuration in case of no matching ref configuration
    rev { version = "\${commit}" }
}

val spekVersion = "2.0.17"
val junitVersion = "5.8.2"
val googleFindBugsVersion = "3.0.2"
val kotestVersion = "5.1.0"

relocateKotlinJsStore()

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform {
                includeEngines("spek2")
            }
//            finalizedBy(tasks.jacocoTestReport)
        }
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
        }


    }
    js(IR) {
        binaries.executable()
        browser()
    }

    nativeTarget { }

    sourceSets {

        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project.dependencies.platform(kotlin("bom")))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation(project.dependencies.platform("io.kotest:kotest-bom:$kotestVersion"))
                implementation("io.kotest:kotest-framework-engine")
                implementation("io.kotest:kotest-assertions-core")
                implementation("io.kotest:kotest-property")
                implementation("io.kotest:kotest-assertions-json")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project.dependencies.platform("org.junit:junit-bom:$junitVersion"))
                implementation("org.junit.jupiter:junit-jupiter")
                runtimeOnly("org.junit.platform:junit-platform-launcher") {
                    because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
                }

                implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
                implementation("org.spekframework.spek2:spek-runner-junit5:$spekVersion")

                implementation("com.google.code.findbugs:jsr305:$googleFindBugsVersion")

                implementation("io.kotest:kotest-runner-junit5")
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.wrapper {
    gradleVersion = "7.4"
    distributionType = Wrapper.DistributionType.ALL
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
