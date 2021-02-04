package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountHolderIdNotFoundException extends Exception {
	public AccountHolderIdNotFoundException(int id) {
		super("Could not find employee " + id);
	}
}
