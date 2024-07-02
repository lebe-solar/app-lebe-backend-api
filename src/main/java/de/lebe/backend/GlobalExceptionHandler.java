package de.lebe.backend;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import de.lebe.backend.api.dto.CommonResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	//NoResourceFoundException
	@ExceptionHandler(NoResourceFoundException.class)
	public void handleException(NoResourceFoundException ex) {
		return;
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse> handleException(Exception ex) {
		LoggerFactory.getLogger(GlobalExceptionHandler.class).error(ex.getMessage());
		LoggerFactory.getLogger(GlobalExceptionHandler.class).error(ExceptionUtils.getStackTrace(ex));
		return new ResponseEntity<>(new CommonResponse("500", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<CommonResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<>(new CommonResponse("500", ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return errors;
	}
}