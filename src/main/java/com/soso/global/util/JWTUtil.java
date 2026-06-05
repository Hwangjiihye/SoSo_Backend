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
    
    @Value("${jwt.expiration}") 
    private Long expiration;
    
    private Algorithm alg;
    private JWTVerifier jwt;
    
    // 생성자 및 비밀키 검증기 초기화 (기존 코드 유지)
    public JWTUtil(@Value("${jwt.secret}") String secret) { 
        this.alg = Algorithm.HMAC256(secret);
        this.jwt = JWT.require(alg).build();
    }
    
    /**
     * ⭕ [변경] 로그인 성공 시 id 문자열 대신 고유 고리 userSeq(Long)를 받아서 토큰 생성!
     */
    public String createToken(Long user_seq) {
        return JWT.create()
                // 💡 Auth0 라이브러리에서는 숫자를 넣을 때 .withClaim(Key, Long) 메서드를 쓰네!
                .withClaim("user_seq", user_seq) 
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(alg);
    }
    
    // 토큰 유효성 검사 (기존 코드 유지)
    public DecodedJWT validation(String token) {
        return jwt.verify(token);
    }
    
    /**
     * ⭕ [신설] 토큰 안에 꽁꽁 숨겨진 userSeq를 Long 타입으로 칼같이 꺼내는 치트키!
     */
    public Long getUserSeq(String token) {
        // 1. 토큰 복호화해서 내용물(DecodedJWT)을 가져옴세
        DecodedJWT decodedJWT = validation(token);
        
        // 2. 🛡️ Auth0에서 클레임을 꺼내면 Claim 객체가 나오는데, 
        //    뒤에 .asLong()을 붙여주면 자바 형변환 지뢰 없이 완벽하게 'Long' 타입으로 변환해 주네!
        return decodedJWT.getClaim("user_seq").asLong();
    }
    
    // (임시 유지) 만약 다른 팀원 코드가 예전 getSubject를 쓰고 있다면 에러 방지용으로 남겨둠세
    @Deprecated
    public String getSubject(String token) {
        return validation(token).getSubject();
    }
}