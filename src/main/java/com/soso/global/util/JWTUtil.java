package com.soso.global.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {
		
		// application.properties에 있는 토큰 만료 시간을 가져옴
		@Value("${jwt.expiration}") // application.properties에서 알아서 퍼옴
		private Long expiration;
		
		private Algorithm alg;
		private JWTVerifier jwt;
		
		// 토큰 서명에 사용할 비밀키를 가져와서 JWT검증기를 만들어둠
		// 생성자 이용해서 초기값 만들기
		public JWTUtil(@Value("${jwt.secret}")String secret) { // 시크릿 값 받아옴
			this.alg = Algorithm.HMAC256(secret);
			this.jwt = JWT.require(alg).build();
		}
		
		// 로그인 성공 시 토큰 생성
		public String createToken(String id) {
			return JWT.create()
					// 토큰 안에 대표값으로 로그인 ID를 넣는 부분.
					.withSubject(id) // withSubject : 대표 데이터를 뜻함, .withClaim : 토큰에 데이터정보를 추가하고 싶으면 키,벨류로 넣기
					.withIssuedAt(new Date())
					.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
					.sign(alg);
		}
		
		// 토큰 유효성 검사 하는 함수(토큰이 진짜인지, 만료되지 않았는지 검사)
		public DecodedJWT validation(String token) {
			return jwt.verify(token);
		}
		
		// subject 편하게 꺼내는 함수(토큰 안에 저장된 ID를 꺼냄)
		public String getSubject(String token) {
			return validation(token).getSubject();
		}
	}
