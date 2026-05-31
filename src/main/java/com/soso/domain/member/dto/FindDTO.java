package com.soso.domain.member.dto;

public class FindDTO {
	
	private String name;
	private String email;
	private String code;
	private String newPassword;
	private String id;
	
	public FindDTO() {}
	public FindDTO(String name, String email, String code, String newPassword, String id) {
		super();
		this.name = name;
		this.email = email;
		this.code = code;
		this.newPassword = newPassword;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
}
