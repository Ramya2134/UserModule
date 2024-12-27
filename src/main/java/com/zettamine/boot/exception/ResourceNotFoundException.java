package com.zettamine.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String resourceName, String fieldName, String resource) {
		super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, resource));
	}
}
