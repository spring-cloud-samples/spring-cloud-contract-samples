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

package contracts.beer.rest;

import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.map;

class shouldReturnStatsForAUser implements Supplier<Contract> {

	@Override
	public Contract get() {
		return Contract.make(c -> {
			c.request(r -> {
				r.method("POST");
				r.url("/stats");
				r.body(map().entry("name", r.anyAlphaUnicode()));
				r.headers(h -> {
					h.contentType(h.applicationJson());
				});
			});
			c.response(r -> {
				r.status(r.OK());
				r.body(map()
						.entry("text",
								"Dear {{{jsonPath request.body '$.name'}}} thanks for your interested in drinking beer")
						.entry("quantity", r.$(r.c(5), r.p(r.anyNumber()))));
				r.headers(h -> {
					h.contentType(h.applicationJson());
				});
			});
		});
	}

}