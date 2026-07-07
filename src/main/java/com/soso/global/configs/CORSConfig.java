package com.soso.global.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer{
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**") // 모든 API 경로에 대해
        .allowedOrigins("http://localhost:5173","https://soso-43d04.web.app","https://soso-43d04.firebaseapp.com")// 리액트 주소 허용
        .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);// 이 주소로부터 들어오는 것은 허용해주겠다.
	}
}
