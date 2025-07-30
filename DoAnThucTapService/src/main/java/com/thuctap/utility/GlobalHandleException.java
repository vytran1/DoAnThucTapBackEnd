package com.thuctap.utility;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.thuctap.common.exceptions.InventoryAlreadyExistException;
import com.thuctap.common.exceptions.OrderNotFoundException;
import com.thuctap.common.exceptions.StatusNotFoundException;
import com.thuctap.common.exceptions.SupplierNotFoundException;
import com.thuctap.security.jwt.JwtValidationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalHandleException extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandleException.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setPath(((ServletWebRequest) request).getRequest().getServletPath());

		List<FieldError> listErrors = ex.getBindingResult().getFieldErrors();
		listErrors.forEach((fieldError) -> error.addError(fieldError.getDefaultMessage()));
		LOGGER.error(ex.getMessage(), ex);
		return new ResponseEntity(error, headers, status);
	}

	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleConstraintViolationException(HttpServletRequest request, Exception exception) {

		ConstraintViolationException conException = (ConstraintViolationException) exception;
		var contrasinInValid = conException.getConstraintViolations();
		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		contrasinInValid.forEach(item -> {
			error.addError(item.getPropertyPath() + ": " + item.getMessage());
		});
		error.setPath(request.getServletPath());

		LOGGER.error(exception.getMessage(), exception);
		return error;
	}

	@ResponseBody
	@ExceptionHandler(JwtValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleJwtValidationException(HttpServletRequest request, Exception exception) {

		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.addError(exception.getMessage());
		error.setPath(request.getServletPath());

		LOGGER.error(exception.getMessage(), exception);
		return error;
	}

	@ResponseBody
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDTO handleIllegalArgumentException(HttpServletRequest request, Exception exception) {

		ErrorDTO error = new ErrorDTO();
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.addError(exception.getMessage());
		error.setPath(request.getServletPath());

		LOGGER.error(exception.getMessage(), exception);
		return error;
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String errorMessage = "Invalid value for parameter '" + ex.getName() + "'. Expected type is "
				+ ex.getRequiredType().getName();
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
		errorDTO.addError(errorMessage);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorDTO> handleRuntimeException(HttpServletRequest request, RuntimeException exception) {
	    ErrorDTO error = new ErrorDTO();
	    error.setTimestamp(new Date());
	    error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    error.addError(exception.getMessage());
	    error.setPath(request.getServletPath());

	    LOGGER.error("RuntimeException: ", exception);
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<?> handleOrderNotFound(OrderNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(InventoryAlreadyExistException.class)
	public ResponseEntity<?> handleInventoryCodeAlreadyExist(InventoryAlreadyExistException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	
	

	@ExceptionHandler(SupplierNotFoundException.class)
	public ResponseEntity<?> handleSupplierNotFound(SupplierNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(StatusNotFoundException.class)
	public ResponseEntity<?> handleStatusNotFound(StatusNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<?> handleIllegalState(IllegalStateException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//	}

}