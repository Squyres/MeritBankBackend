package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MaxAccountsReachedException extends Exception {

	public MaxAccountsReachedException() {
		super("Maximum number of this type of account already exist.");
	}

	public MaxAccountsReachedException(String msg) {
		super(msg);
	}

}