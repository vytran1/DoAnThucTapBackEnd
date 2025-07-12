package com.thuctap.account.dto;

public class ChangePasswordRequest {
	
	private String oldPassword;
	private String newPassword;
	
	
	public ChangePasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}


	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	
	
}
