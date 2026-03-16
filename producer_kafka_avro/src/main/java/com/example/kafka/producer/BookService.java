package com.example.kafka.producer;

import java.util.Map;

import com.example.kafka.avro.Book;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
class BookService {

	private final KafkaTemplate<String, Book> kafkaTemplate;

	BookService(KafkaTemplate<String, Book> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	void bookReturned(String isbn, String title, String author) {
		Book payload = Book.newBuilder().setIsbn(isbn).setTitle(title).setAuthor(author)
				.build();

		// @formatter:off
		MessageHeaders headers = new MessageHeaders(Map.of(
				KafkaHeaders.TOPIC, "book.returned",
				"X-Correlation-Id", "abc-123-def",
				"X-Source-System", "library-service",
				"X-Event-Type", "BOOK_RETURNED"
		));
		// @formatter:on

		Message<Book> msg = MessageBuilder.createMessage(payload, headers);
		kafkaTemplate.send(msg);
	}

}
