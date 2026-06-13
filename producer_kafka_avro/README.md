# Kafka + Avro Producer — Spring Cloud Contract samples

This module contains a Spring Boot application that publishes a `book.returned` event to a Kafka topic
whenever a book is returned to a library.
The message payload is an [Avro](https://avro.apache.org/)-encoded `Book` object,
serialized using the Confluent `KafkaAvroSerializer`.

**Source:** [`BookService`](src/main/java/com/example/kafka/producer/BookService.java)

---

## What is a contract test (producer side)?

A contract is a description of what a producer promises to publish —
which topic, which headers, and what the message body looks like.
Spring Cloud Contract reads the contract, generates a JUnit test,
and runs it against the real producer code.
If the producer still publishes the right message, the test passes and a **stubs jar** is produced.
The consumer module uses that stubs jar to replay realistic messages in its own tests
without needing a live producer.

---

## Background: how Avro works on the wire

Avro does not embed the schema inside every message.
Instead, Confluent's serializer registers the schema in a **Schema Registry**
and writes a compact 5-byte prefix —
one magic byte (`0x00`) plus a 4-byte schema ID —
before the actual binary payload.
The deserializer on the other end reads that ID,
fetches the schema from the registry,
and decodes the rest of the bytes.

This matters for testing because there are two natural ways to express the expected message in a contract:

---

## Two flavors of contract

### Flavor 1 — JSON (human-readable)

**Contract:** [`contracts/json/bookReturnedJson.groovy`](src/test/resources/contracts/json/bookReturnedJson.groovy)

The contract body is written as plain JSON fields:

```groovy
body(
    isbn: '978-1234567890',
    title: 'Contract Testing for Dummies',
    author: 'John Doe'
)
```

**How the test works:**

1. SCC calls `publishBookReturned()` on the test base class.
2. The producer serializes the `Book` to Avro bytes and sends them to the Kafka topic.
3. The test base class consumes the message via a `KafkaListener`,
   which deserializes the Avro bytes back into a `Book` object.
4. SCC serializes that object to JSON and compares it field-by-field against the contract body.

**Trade-off:** Two extra JSON ↔ Avro conversions happen during the test,
but failure messages are easy to read —
you see exactly which field had the wrong value.

**Test base class:** [`AvroJsonContractTestBase`](src/test/java/com/example/kafka/producer/AvroJsonContractTestBase.java)

---

### Flavor 2 — Binary (exact wire format)

**Contract:** [`contracts/binary/bookReturnedBinary.groovy`](src/test/resources/contracts/binary/bookReturnedBinary.groovy)

The contract body references a pre-serialized binary file:

```groovy
body(fileAsBytes("bookReturnedMessage.bin"))
```

**How the test works:**

1. SCC calls `publishBookReturned()` on the test base class.
2. The producer serializes the `Book` to Avro bytes and sends them to the Kafka topic.
3. The test base class consumes the raw bytes
   (using `ByteArrayDeserializer`, bypassing Avro decoding).
4. SCC compares those raw bytes byte-for-byte against
   [`bookReturnedMessage.bin`](src/test/resources/contracts/binary/bookReturnedMessage.bin).

**Trade-off:** The assertion is an exact binary comparison —
no JSON conversions, no room for serialization drift —
but failure messages show raw bytes and are harder to interpret.

**Test base class:** [`AvroBinaryContractTestBase`](src/test/java/com/example/kafka/producer/AvroBinaryContractTestBase.java)

#### Regenerating the `.bin` fixture

The `.bin` file must be committed to source control.
Run [`AvroBinaryTestFixture`](src/test/java/com/example/kafka/producer/AvroBinaryTestFixture.java)
whenever the `Book` schema or the test data changes to produce a fresh file:

```bash
./mvnw exec:java -Dexec.mainClass=com.example.kafka.producer.AvroBinaryTestFixture
```