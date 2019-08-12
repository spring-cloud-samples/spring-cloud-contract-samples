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

package contracts.beer.intoxication;

import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.map;

class _2_tipsy implements Supplier<Contract> {
	@Override
	public Contract get() {
		return Contract.make(c -> {
			c.description("Represents first step of getting fully drunk\n" + "\n"
					+ "given:\n" + "\tyou didn't drink anything\n" + "when:\n"
					+ "\tyou get a beer\n" + "then:\n" + "\tyou'll be tipsy");
			c.request(r -> {
				r.method("POST");
				r.url("/beer");
				r.body(map().entry("name", "marcin"));
				r.headers(h -> {
					h.contentType(h.applicationJson());
				});
			});
			c.response(r -> {
				r.status(r.OK());
				r.body(map()
						.entry("previousStatus", "TIPSY")
						.entry("currentStatus",
								"DRUNK"));
				r.headers(h -> {
					h.contentType(h.applicationJson());
				});
			});
		});
	}
}
