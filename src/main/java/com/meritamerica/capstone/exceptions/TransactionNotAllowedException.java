package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransactionNotAllowedException extends Exception {

	public TransactionNotAllowedException() {
		super("Unable to complete transactions on CDAccounts");
	}

	public TransactionNotAllowedException(String msg) {
		super(msg);
	}

}