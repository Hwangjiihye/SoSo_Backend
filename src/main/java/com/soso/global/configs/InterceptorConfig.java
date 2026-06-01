package com.soso.global.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.soso.global.common.TokenValidator;

//TokenValidator를 실제로 적용시키는 설정 파일
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Autowired
	private TokenValidator Interceptor;
	
	// Dispatcher랑 Controller 사이에 interceptor 설치
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 스프링 설정 클래스
		// 의미 : 모든 요청에 TokenValidator 적용  단, /auth/login은 제외
		// 그래서 로그인은 토큰 없이 가능하고, 나머지는 토큰 필요.
		registry.addInterceptor(Interceptor)
		.addPathPatterns("/**") // 여기서는 post : interceptor 일단 들어오게하고 tokenValidator에서 따로 설정
		.excludePathPatterns("/auth/**", "/api/member/**"); // 이 경로로 들어오는건 interceptor를 배제해라(로그인)
	}

}
