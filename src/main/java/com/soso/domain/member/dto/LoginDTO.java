package com.soso.domain.member.dto;

public class LoginDTO {
	
	private String id;
	private String pw;
	private String user_type;    // BUSINESS, PARTNER, ADMIN
	
	public LoginDTO() {}
	public LoginDTO(String id, String pw, String user_type) {
		super();
		this.id = id;
		this.pw = pw;
		this.user_type = user_type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
}
