# Kafka + Avro Consumer — Spring Cloud Contract samples

This module contains a Spring Boot application that listens to the `book.returned` Kafka topic.
When a message arrives, it is deserialized from Avro into a `Book` object
and passed to `EmailService` to send a notification email.

**Source:** [`BooksReturnedListener`](src/main/java/com/example/kafka/consumer/BooksReturnedListener.java)

---

## What is a collaboration test (consumer side)?

A collaboration test verifies that the consumer can correctly handle the messages
that the producer promised to publish.
Instead of running a live producer,
it uses the **stubs jar** produced by the producer's contract tests.
Spring Cloud Contract's Stub Runner loads that jar,
and the test triggers a specific message label to replay a realistic message on the Kafka topic.
The consumer processes it just as it would in production.

**Prerequisite:** build and install the producer stubs first:

---

## Background: how Avro works on the wire

Confluent's serializer writes a compact 5-byte prefix before each message —
one magic byte (`0x00`) plus a 4-byte schema ID —
and registers the schema in a **Schema Registry**.
The deserializer reads that ID, fetches the schema, and decodes the rest of the bytes.
Tests in this module use a mock schema registry (`mock://test`)
so no real registry server is needed.

---

## Two flavors of collaboration test

The producer module defines two contracts, one per flavor.
There is a matching collaboration test here for each.

### Flavor 1 — JSON (human-readable)

**Test:** [`AvroJsonCollaborationTest`](src/test/java/com/example/kafka/consumer/AvroJsonCollaborationTest.java)
**Triggered label:** `book_returned`

**How the test works:**

1. Stub Runner triggers the `book_returned` label from the stubs jar.
2. The test's `MessageVerifierSender` receives a JSON string from the stub
   (the contract body fields),
   converts it into a `Book` object,
   and sends it to Kafka using `KafkaAvroSerializer`.
3. `BooksReturnedListener` receives the Avro-deserialized `Book`
   and calls `EmailService.sendEmail()`.
4. The test asserts that `EmailService` was called with the expected email content.

**Trade-off:** Two extra JSON ↔ Avro conversions happen during the test,
but failure messages are easy to read —
you see exactly which field had the wrong value.

---

### Flavor 2 — Binary (exact wire format)

**Test:** [`AvroBinaryCollaborationTest`](src/test/java/com/example/kafka/consumer/AvroBinaryCollaborationTest.java)
**Triggered label:** `book_returned_binary`

**How the test works:**

1. Stub Runner triggers the `book_returned_binary` label,
   which delivers the raw bytes from the producer's pre-serialized
   [`bookReturnedMessage.bin`](../producer_kafka_avro/src/test/resources/contracts/binary/bookReturnedMessage.bin) fixture.
2. The test's `MessageVerifierSender` puts those bytes directly on the Kafka topic
   using `ByteArraySerializer` —
   no Avro serialization happens in the test itself.
3. `BooksReturnedListener` receives the bytes,
   `KafkaAvroDeserializer` decodes them into a `Book`,
   and `EmailService.sendEmail()` is called.
4. The test asserts that `EmailService` was called with the expected email content.

**Trade-off:** The exact bytes that the producer emits in production
travel through the consumer's full deserialization stack,
with no intermediary conversion.
The downside is that failure messages show raw bytes and are harder to interpret.

> **Note:** Before the raw bytes can be deserialized,
> the `Book` Avro schema must be registered in the mock schema registry for this JVM.
> The test's `TestConfig` handles this automatically via a `@PostConstruct` method —
> see [`AvroBinaryCollaborationTest.TestConfig#registerBookSchema`](src/test/java/com/example/kafka/consumer/AvroBinaryCollaborationTest.java)
> for the explanation.
