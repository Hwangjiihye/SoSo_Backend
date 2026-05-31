package com.soso.domain.member.dto;

public class FindDTO {
	
	private String name;
	private String email;
	private String code;
	
	public FindDTO() {}
	public FindDTO(String name, String email, String code) {
		super();
		this.name = name;
		this.email = email;
		this.code = code;
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
}
