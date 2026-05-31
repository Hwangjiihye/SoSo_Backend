package com.soso.domain.member.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soso.domain.member.dao.LoginDAO;
import com.soso.domain.member.dto.LoginDTO;

@Service
public class LoginService {
	
	@Autowired
	private LoginDAO LoginDAO;
	
	@Autowired
	private PasswordEncoder PasswordEncoder;
	
	public Map<String, Object> toLogin(LoginDTO dto) {
		
		// 1. 아이디 + 회원유형으로 회원 조회
		Map<String, Object> member = LoginDAO.toLogin(dto);
		
		// 2. 아이디 또는 회원 유형이 안맞으면 실패
		if(member == null) {
			return null;
		}
		
		// 3. DB에 저장된 암호화 비밀번호 가져오기
		String DBPassword = (String)member.get("password");
		
		// 4. 입력한 비밀번호와 DB 암호화 비밀번호 비교
		boolean isMatch = PasswordEncoder.matches(dto.getPw(), DBPassword);
		
		// 5. 비밀번호가 틀리면 실패
		if(!isMatch) {
			return null;
		}
		
		// 6. 전부 맞으면 회원 정보 반환
		return member;
		
//		return LoginDAO.toLogin(dto);
	}
}
