package com.example;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuffController {

	private static final String LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\nLorem Ipsum has been the industry's standard dummy text ever since the 1500s...\n";

	@PostMapping(value = "/stuff",
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	Stuff stuff(@RequestBody Stuff stuff) {
		Assert.isTrue(stuff.comment.equals(LOREM_IPSUM), "Should have a matching comment");
		return new Stuff(LOREM_IPSUM, "It worked", "success");
	}
}

class Stuff {
	public String comment;
	public String message;
	public String status;

	public Stuff(String comment, String message, String status) {
		this.comment = comment;
		this.message = message;
		this.status = status;
	}

	public Stuff() {
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}