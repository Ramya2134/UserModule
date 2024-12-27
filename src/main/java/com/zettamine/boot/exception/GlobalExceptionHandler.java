package com.zettamine.boot.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.zettamine.boot.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(ResounceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> resourceAlreadyExists(WebRequest request, ResounceAlreadyExistsException ex){
		ErrorResponseDto errorDto = new ErrorResponseDto();
		errorDto.setApiPath(request.getDescription(false));
		errorDto.setErrorCode(HttpStatus.BAD_REQUEST);
		errorDto.setErrorMessage(ex.getMessage());
		errorDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errorDto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> resourceNotFound(WebRequest request, ResourceNotFoundException ex){
		ErrorResponseDto errResponseDto = new ErrorResponseDto();
		errResponseDto.setApiPath(request.getDescription(false));
		errResponseDto.setErrorCode(HttpStatus.BAD_REQUEST);
		errResponseDto.setErrorMessage(ex.getMessage());
		errResponseDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errResponseDto, HttpStatus.BAD_REQUEST);
	}

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> erros = new HashMap<>();
		ex.getBindingResult().getFieldErrors().stream().map(m-> erros.put(m.getField(), m.getDefaultMessage()));
		
		return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
	}

}
