import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	id("maven-publish")
	// aligned with Gradle
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

// tag::deps_repos[]
repositories {
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://repo.spring.io/release") }
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}
// end::deps_repos[]

// tag::dep_mgmt[]
dependencyManagement {

	val BOM_VERSION: String by project

	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$BOM_VERSION")
	}
}
// end::dep_mgmt[]

// tag::deps[]
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
	testImplementation("org.springframework.cloud:spring-cloud-contract-spec-kotlin")
	//	for easier testing of multipart
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "junit", module = "junit")
	}

	// for compatibility
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
// end::deps[]

tasks {
	test {
		useJUnitPlatform()
		systemProperty("spring.profiles.active", "gradle")
		testLogging {
			exceptionFormat = TestExceptionFormat.FULL
		}
		afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
			if (desc.parent == null) {
				if (result.testCount == 0L) {
					throw IllegalStateException("No tests were found. Failing the build")
				}
				else {
					println("Results: (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
				}
			} else { /* Nothing to do here */ }
		}))
	}
}

tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifact(tasks.named("bootJar"))

			// https://github.com/spring-gradle-plugins/dependency-management-plugin/issues/273
			versionMapping {
				usage("java-api") {
					fromResolutionOf("runtimeClasspath")
				}
				usage("java-runtime") {
					fromResolutionResult()
				}
			}
		}
	}
}
