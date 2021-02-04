package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsAvailableBalanceException extends Exception {

	public ExceedsAvailableBalanceException() {
		super("Amount is greater than available balance");
	}

	public ExceedsAvailableBalanceException(String msg) {
		super(msg);
	}
}