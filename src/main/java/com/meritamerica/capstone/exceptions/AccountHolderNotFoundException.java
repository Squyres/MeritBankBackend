package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountHolderNotFoundException extends Exception {

	public AccountHolderNotFoundException() {
		super("Could not find employee");
	}

	public AccountHolderNotFoundException(int id) {
		super("Could not find employee " + id);
	}
}