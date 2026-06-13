package com.example.kafka.consumer;

import com.example.kafka.avro.Book;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class BooksReturnedListener {

	private final EmailService emailService;

	BooksReturnedListener(EmailService emailService) {
		this.emailService = emailService;
	}

	@KafkaListener(topics = "book.returned")
	public void sendEmailOnBookReturned(Book book) {
		String emailBody = """
				Dear User,
				
				The book you borrowed has been successfully returned:
				Title: %s, Author: %s, ISBN: %s
				
				""".formatted(book.getTitle(), book.getAuthor(), book.getIsbn());

		emailService.sendEmail(emailBody);
	}

}
