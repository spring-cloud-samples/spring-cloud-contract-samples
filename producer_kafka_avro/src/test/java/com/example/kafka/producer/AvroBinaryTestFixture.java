package com.example.kafka.producer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.example.kafka.avro.Book;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

/**
 * Generates {@code bookReturnedBinary.bin} — a pre-serialized Avro fixture used by the
 * {@code bookReturnedBinary.groovy} contract.
 *
 * <p>Run {@code main} once from the project root whenever the {@code Book} schema or the
 * test data changes, then commit the updated {@code .bin} file. The contract verifier
 * compares the raw bytes received on the Kafka topic against this file, so no Avro
 * schema knowledge is required in the contract itself.
 *
 * <p>The output uses the Confluent wire format:
 * {@code [0x00][4-byte schema ID][Avro binary payload]}.
 */
class AvroBinaryTestFixture {

	public static void main(String[] args) throws Exception {
		var payload = Book.newBuilder()
				.setIsbn("978-1234567890")
				.setTitle("Contract Testing for Dummies")
				.setAuthor("John Doe")
				.build();

		var bytes = toAvroBytes("book.returned", payload, "mock://test");
		var path = Paths.get("producer_kafka_avro/src/test/resources/contracts/binary/bookReturnedMessage.bin");

		saveFile(bytes, path);
	}

	private static byte[] toAvroBytes(String topic, Object payload,
			String schemaRegistryUrl) {
		try (KafkaAvroSerializer serializer = new KafkaAvroSerializer()) {
			serializer.configure(Map.of("schema.registry.url", schemaRegistryUrl), false);
			return serializer.serialize(topic, payload);
		}
	}

	private static void saveFile(byte[] bytes, Path output) throws IOException {
		if (!Files.exists(output)) {
			Files.createDirectories(output.getParent());
			Files.createFile(output);
		}
		else {
			throw new IOException("Output file already exists: " + output.toAbsolutePath());
		}

		try (FileOutputStream fos = new FileOutputStream(output.toFile())) {
			fos.write(bytes);
		}
		System.out.printf("Written %d bytes to %s%n", bytes.length, output.toAbsolutePath());
	}

}
