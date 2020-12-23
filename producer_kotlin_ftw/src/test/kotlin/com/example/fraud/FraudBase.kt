/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.fraud;

// remove::start[]
import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
import org.junit.jupiter.api.BeforeEach;
// remove::end[]

open class FraudBase {

// remove::start[]
	@BeforeEach
	fun setup() {
		// https://github.com/spring-cloud/spring-cloud-contract/issues/1428
		val encoderConfig: EncoderConfig = EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
		val restAssuredConf: RestAssuredMockMvcConfig = RestAssuredMockMvcConfig().encoderConfig(encoderConfig)
		RestAssuredMockMvc.config = restAssuredConf
		RestAssuredMockMvc.standaloneSetup(FraudDetectionController(),
				FraudStatsController(stubbedStatsProvider()))
	}

	private fun stubbedStatsProvider(): StatsProvider {
		return object : StatsProvider {
			override fun count(fraudType: FraudType): Int {
				return when (fraudType) {
					FraudType.DRUNKS -> 100
					FraudType.ALL -> 200
					else -> 0
				}
			}
		}
	}

	fun assertThatRejectionReasonIsNull(rejectionReason: Any?) {
		assert(rejectionReason == null)
	}

	// remove::end[]

}
