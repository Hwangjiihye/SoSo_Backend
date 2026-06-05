package com.soso.domain.mypage.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.mypage.dto.BusinessMypageDTO;

@Repository
public class BusinessMypageDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.domain.mypage.repository.BusinessMypageRepository";

    public BusinessMypageDTO getBusinessInfo(Long userSeq) {
        return sqlSession.selectOne(NAMESPACE + ".getBusinessInfo", userSeq);
    }
}
