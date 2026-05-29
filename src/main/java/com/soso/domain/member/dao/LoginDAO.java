package com.soso.domain.member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.member.dto.LoginDTO;

@Repository
public class LoginDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public int toLogin(LoginDTO dto) {
		return mybatis.selectOne("Login.toLogin", dto);
	}
	
	
}
