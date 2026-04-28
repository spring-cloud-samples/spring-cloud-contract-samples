/*
 * Copyright 2013-present the original author or authors.
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

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description 'Should publish a book returned event to Kafka'
	label 'book_returned'
	input {
		triggeredBy('publishBookReturned()')
	}
	outputMessage {
		sentTo('book.returned')
		headers {
			header('X-Correlation-Id', 'abc-123-def')
			header('X-Source-System', 'library-service')
			header('X-Event-Type', 'BOOK_RETURNED')
		}
		body(
			isbn: '978-1234567890',
			title: 'Contract Testing for Dummies',
			author: 'John Doe'
		)
	}
}
