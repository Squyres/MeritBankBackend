package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotCloseAccountException extends Exception {

	public CannotCloseAccountException() {
		super("Unable to close Savings Account.");
	}

	public CannotCloseAccountException(String msg) {
		super(msg);
	}

}