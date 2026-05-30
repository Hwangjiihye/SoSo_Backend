package com.soso.domain.member.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.member.dao.LoginDAO;
import com.soso.domain.member.dto.LoginDTO;

@Service
public class LoginService {
	
	@Autowired
	private LoginDAO LoginDAO;
	
	
	public Map<String, Object> toLogin(LoginDTO dto) {
		
		return LoginDAO.toLogin(dto);
	}
	
	

}
