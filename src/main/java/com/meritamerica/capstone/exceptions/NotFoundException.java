package com.meritamerica.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

	public NotFoundException() {
		super("Item not found");
	}

	public NotFoundException(String msg) {
		super(msg);
	}

}