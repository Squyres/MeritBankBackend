package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsCombinedBalanceLimitException extends Exception {

	public ExceedsCombinedBalanceLimitException() {
		super("Cannot have combined checking and savings account balances >250,000");
	}

	public ExceedsCombinedBalanceLimitException(String msg) {
		super(msg);
	}

}