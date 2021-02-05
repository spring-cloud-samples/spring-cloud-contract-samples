/*
 * Copyright 2018-2019 the original author or authors.
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

package com.example;

import org.assertj.core.api.BDDAssertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// remove::start[]
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.kafka.test.context.EmbeddedKafka;
// remove::end[]
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// remove::start[]
@AutoConfigureStubRunner(ids = "com.example:beer-api-producer-kafka", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@EmbeddedKafka(topics = "topic1")
// remove::end[]
@ActiveProfiles("test")
public class ApplicationTests {

	// remove::start[]
	@Autowired
	StubTrigger trigger;
	@Autowired
	Application application;

	@Test
	public void consumesFooAsObject() {
		this.trigger.trigger("trigger");

		Awaitility.await().untilAsserted(() -> {
			BDDAssertions.then(this.application.storedFoo).isNotNull();
			BDDAssertions.then(this.application.storedFoo.getFoo()).contains("example");
		});
	}

	@Test
	public void consumesFooAsMessage() {
		this.trigger.trigger("triggerMessage");

		Awaitility.await().untilAsserted(() -> {
			BDDAssertions.then(this.application.storedFooMessage).isNotNull();
			BDDAssertions.then(this.application.storedFooMessage.getPayload().getFoo()).contains("example");
			BDDAssertions.then(this.application.storedFooMessage.getHeaders().containsKey("kafka_receivedMessageKey"));
			BDDAssertions.then(this.application.storedFooMessage.getHeaders().get("kafka_receivedMessageKey"))
					.isEqualTo("key-example");
		});
	}

	// remove::end[]

}
