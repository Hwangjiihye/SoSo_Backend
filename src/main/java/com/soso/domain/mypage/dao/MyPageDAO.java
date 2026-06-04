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

    public PartnerProfileDTO getPartnerProfile(Long user_seq) {
        return mybatis.selectOne(NAMESPACE + ".getPartnerProfile", user_seq);
    }

    public int updateUser(com.soso.domain.mypage.dto.PartnerUpdateDTO updateDto) {
        return mybatis.update(NAMESPACE + ".updateUser", updateDto);
    }

    public int updateStore(com.soso.domain.mypage.dto.PartnerUpdateDTO updateDto) {
        return mybatis.update(NAMESPACE + ".updateStore", updateDto);
    }
}
