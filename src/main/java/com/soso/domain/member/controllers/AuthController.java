package com.soso.domain.member.controllers;

import java.util.HashMap;
import java.util.List;
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
import com.soso.global.util.RedisService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JWTUtil jwt;
	
	@Autowired
	private LoginService LoginServ;

	@Autowired
	private RedisService redisService;
	
	@Value("${jwt.refresh-expiration}")
	private Long refreshExpiration;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> toLogin(@RequestBody LoginDTO dto, HttpServletResponse response) {
		
		
		// 로그인 시도
	    // 아이디, 비밀번호가 맞으면 회원 정보(Map)를 반환
	    // 틀리면 null 반환
	    Map<String, Object> member = LoginServ.toLogin(dto);
	    
		
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
			String userType = String.valueOf(member.get("user_type"));
			String token = jwt.createToken(userSeq, userType);
			
			// Refresh Token 생성 및 Redis 저장
			String refreshToken = jwt.createRefreshToken(userSeq, userType);
			redisService.setValuesWithTimeout("RT:" + userSeq, refreshToken, refreshExpiration);
			
			// Refresh Token 쿠키 발급
			ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
					.maxAge(refreshExpiration / 1000)
					.path("/")
					.secure(true) // HTTPS (or localhost)
					.sameSite("Strict")
					.httpOnly(true)
					.build();
			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
			
			// 보안을 위해 프론트로 보내기 전 비밀번호 제거
			member.remove("password");
			
			// 응답 데이터 추가
			result.put("token", token); // JWT 토큰
			result.put("user_seq", userSeq); // 회원 id
			result.put("user_type", member.get("user_type")); // 회원 유형
			result.put("user_nickname", member.get("nickname"));
			result.put("company_name", member.get("company_name"));
//			result.put("member", member); // 회원 및 매장 전체 정보 (password 제외됨)
			
			List<Integer> storeList = LoginServ.getStoreListByUserSeq(userSeq);
			result.put("selectedStoreSeq", storeList.get(0));
			
			
			return ResponseEntity.ok(result);
		}
		// 정보가 틀리다면 토큰 인증 실패
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/reissue")
	public ResponseEntity<Map<String, Object>> reissue(
			@CookieValue(value = "refreshToken", required = false) String refreshToken,
			HttpServletResponse response) {
		
		if (refreshToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		try {
			// Refresh Token 유효성 검증
			jwt.validation(refreshToken);
			
			Long userSeq = jwt.getUserSeq(refreshToken);
			String userType = jwt.getUserType(refreshToken);
			
			// Redis 값과 대조
			String savedToken = redisService.getValues("RT:" + userSeq);
			if (savedToken == null || !savedToken.equals(refreshToken)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			
			// 새 Access Token 발급
			String newAccessToken = jwt.createToken(userSeq, userType);
			
			// 💡 새 Refresh Token 발급 및 Redis 갱신 (수명 연장)
			String newRefreshToken = jwt.createRefreshToken(userSeq, userType);
			redisService.setValuesWithTimeout("RT:" + userSeq, newRefreshToken, refreshExpiration);
			
			// 💡 새 Refresh Token 쿠키 세팅
			ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
					.maxAge(refreshExpiration / 1000)
					.path("/")
					.secure(true) // HTTPS (or localhost)
					.sameSite("Strict")
					.httpOnly(true)
					.build();
			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
			
			Map<String, Object> result = new HashMap<>();
			result.put("token", newAccessToken);
			
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			try {
				String token = authHeader.substring(7);
				Long userSeq = jwt.getUserSeq(token);
				
				// Redis에서 삭제
				redisService.deleteValues("RT:" + userSeq);
			} catch (Exception e) {
				// 이미 만료된 토큰일 수 있으므로 예외 발생 시 무시하고 쿠키만 지움
			}
		}
		
		// 쿠키 삭제
		ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
				.maxAge(0)
				.path("/")
				.secure(true)
				.sameSite("Strict")
				.httpOnly(true)
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		
		return ResponseEntity.ok().build();
	}
}
