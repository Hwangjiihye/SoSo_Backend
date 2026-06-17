package com.soso.domain.member.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.member.dto.LoginDTO;

@Repository
public class LoginDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public Map<String, Object> toLogin(LoginDTO dto) {
		System.out.println(dto.getUser_type());
		
		
		return mybatis.selectOne("Login.toLogin", dto);
	}
	public List<Integer> getStoreListByUserSeq(Long userSeq) {
	    // "LoginMapper"는 실제 XML의 namespace로 맞춰주게나
	    return mybatis.selectList("Login.getStoreListByUserSeq", userSeq);
	}
	
}
