package com.zettamine.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResounceAlreadyExistsException extends RuntimeException {

	public ResounceAlreadyExistsException(String message) {
		super(message);
	}

}
