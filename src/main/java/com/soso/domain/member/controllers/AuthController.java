package com.soso.domain.member.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.member.dto.LoginDTO;
import com.soso.domain.member.services.LoginService;
import com.soso.global.util.JWTUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private LoginService LoginServ;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> toLogin(@RequestBody LoginDTO dto) {
		
		System.out.println("로그인 아이디 : " + dto.getId() + "로그인 비밀번호 : " + dto.getPw());
		
		int member = LoginServ.toLogin(dto);
		
		if(member > 0) { // 0이면 성공, 1은 실패
			Map<String, String> result = new HashMap<>();
			
			String token = jwt.createToken(dto.getId());
			result.put("token", token);
			result.put("id", dto.getId());
			
			System.out.println(token);
			return ResponseEntity.ok(result);
		}
		// 정보가 틀리다면 토큰 인증 실패
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
