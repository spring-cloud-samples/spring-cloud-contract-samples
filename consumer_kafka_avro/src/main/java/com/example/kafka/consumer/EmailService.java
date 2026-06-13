package com.example.kafka.consumer;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	public void sendEmail(String emailBody) {
		// Simulate sending an email
		System.out.println("Sending email:\n" + emailBody);
	}

}
