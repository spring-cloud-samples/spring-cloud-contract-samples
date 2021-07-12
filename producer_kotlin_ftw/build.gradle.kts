import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	
	id("org.springframework.cloud.contract")
	id("maven-publish")
	// Kotlin version needs to be aligned with Gradle
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://repo.spring.io/release") }
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}



dependencyManagement {

	val BOM_VERSION: String by project

	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$BOM_VERSION")
	}
}



dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	
	
	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
	testImplementation("org.springframework.cloud:spring-cloud-contract-spec-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "junit", module = "junit")
	}

	// for compatibility
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
}




contracts {
	testFramework.set(org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5)
	packageWithBaseClasses.set("com.example.fraud")
}


tasks.withType<Delete> {
	doFirst {
		delete("~/.m2/repository/com/example/producer-kotlin-ftw-gradle")
	}
}


tasks {
  contractTest {
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

			artifact(tasks.named("verifierStubsJar"))

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
