package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameAlreadyExistsException extends Exception {

	public UsernameAlreadyExistsException() {
		super("Username already exists");
	}

	public UsernameAlreadyExistsException(String msg) {
		super(msg);
	}

}