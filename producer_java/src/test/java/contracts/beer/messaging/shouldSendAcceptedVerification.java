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

package contracts.beer.messaging;

import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.map;

class shouldSendAcceptedVerification implements Supplier<Contract> {

	@Override
	public Contract get() {
		return Contract.make(c -> {
			c.description(
					"Sends a positive verification message when person is eligible to get the beer\n"
							+ "given:\n" + "\tclient is old enough\n" + "when:\n"
							+ "\the applies for a beer\n" + "then:\n"
							+ "\twe'll send a message with a positive verification");
			c.label("accepted_verification");
			c.input(i -> {
				// the contract will be triggered by a method
				i.triggeredBy("clientIsOldEnough()");
			});
			// output message of the contract
			c.outputMessage(o -> {
				// destination to which the output message will be sent
				o.sentTo("verifications");
				// the body of the output message
				o.body(map().entry("eligible", true));
				o.headers(h -> {
					h.messagingContentType(h.applicationJson());
				});
			});
		});
	}

}