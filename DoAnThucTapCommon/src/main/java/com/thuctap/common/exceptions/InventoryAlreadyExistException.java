package com.thuctap.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InventoryAlreadyExistException extends RuntimeException{
	
	public InventoryAlreadyExistException(String message) { 
		super(message);
	}
}
