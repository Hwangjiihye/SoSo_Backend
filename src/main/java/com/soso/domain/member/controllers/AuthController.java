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
	public ResponseEntity<Map<String, Object>> toLogin(@RequestBody LoginDTO dto) {
		
		System.out.println("로그인 아이디 : " + dto.getUser_seq() + "로그인 비밀번호 : " + dto.getPw());
		
		// 로그인 시도
	    // 아이디, 비밀번호가 맞으면 회원 정보(Map)를 반환
	    // 틀리면 null 반환
	    Map<String, Object> member = LoginServ.toLogin(dto);
	    
	    System.out.println("조회 결과 : " + member);
		
		if(member != null) { 
			// [추가] 탈퇴 회원 응답 처리 (isWithDraw 상태면 바로 반환)
			if ("isWithDraw".equals(member.get("status"))) {
				return ResponseEntity.ok(member);
			}

			// 프론트로 보낼 응답 데이터
			Map<String, Object> result = new HashMap<>();
			
			// JWT 토큰 생성
			// ⭕ member.get()이 Integer든 Long이든 상관없이 문자열로 바꾼 뒤 깔끔하게 Long으로 추출!
			Long userSeq = Long.parseLong(String.valueOf(member.get("user_seq")));
			String token = jwt.createToken(userSeq);
			
			// 응답 데이터 추가
			result.put("token", token); // JWT 토큰
			result.put("user_seq", userSeq); // 회원 id
			result.put("user_type", member.get("user_type")); // 회원 유형
			
			System.out.println("dto id = " + dto.getId());
			System.out.println("dto pw = " + dto.getPw());
			System.out.println("userType : " + dto.getUser_type());
			System.out.println("조회 결과 = " + member.get("user_seq"));
			
			System.out.println(token);
			return ResponseEntity.ok(result);
		}
		// 정보가 틀리다면 토큰 인증 실패
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
