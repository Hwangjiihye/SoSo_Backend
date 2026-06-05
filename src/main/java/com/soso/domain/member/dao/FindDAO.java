package com.soso.domain.member.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.member.dto.FindDTO;

@Repository
public class FindDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public int findId(FindDTO dto) {
		return mybatis.selectOne("Find.findId", dto);
	}
	
	public String getIdByEmail(String email) {
	    return mybatis.selectOne("Find.getIdByEmail", email);
	}
	
	public int findPw(FindDTO dto) {
	    return mybatis.selectOne("Find.findPw", dto);
	}
	
	public int updatePassword(FindDTO dto) {
		return mybatis.update("Find.updatePassword", dto);
	}

}
