package com.soso.domain.mypage.dao;

import com.soso.domain.mypage.dto.PartnerProfileDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyPageDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    private static final String NAMESPACE = "com.soso.domain.mypage.dao.MyPageDAO";

    public PartnerProfileDTO getPartnerProfile(String userId) {
        return mybatis.selectOne(NAMESPACE + ".getPartnerProfile", userId);
    }
}
