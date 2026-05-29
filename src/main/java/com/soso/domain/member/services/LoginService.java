package com.soso.domain.member.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.member.dao.LoginDAO;
import com.soso.domain.member.dto.LoginDTO;

@Service
public class LoginService {
	
	@Autowired
	private LoginDAO LoginDAO;
	
	
	public int toLogin(LoginDTO dto) {
		return LoginDAO.toLogin(dto);
	}
	
	

}
