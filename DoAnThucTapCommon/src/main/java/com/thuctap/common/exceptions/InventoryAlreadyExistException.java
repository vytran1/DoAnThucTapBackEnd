package com.thuctap.common.exceptions;



public class InventoryAlreadyExistException extends RuntimeException{
	
	public InventoryAlreadyExistException(String message) { 
		super(message);
	}
}
