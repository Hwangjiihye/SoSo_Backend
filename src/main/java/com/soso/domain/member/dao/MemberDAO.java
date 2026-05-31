package com.soso.domain.member.dao;

import com.soso.domain.member.dto.SignUpDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    // XML Mapper의 namespace와 일치해야 함
    private static final String NAMESPACE = "com.soso.domain.member.dao.MemberDAO";

    /**
     * 사업자 회원가입 정보를 DB에 저장합니다.
     */
    public int insertMember(SignUpDto signUpDto) {
        return mybatis.insert(NAMESPACE + ".insertMember", signUpDto);
    }

    /**
     * 회원가입 후 생성된 스토어 이미지 URL을 업데이트합니다.
     */
    public int updateStoreImage(SignUpDto signUpDto) {
        return mybatis.update(NAMESPACE + ".updateStoreImage", signUpDto);
    }

    /**
     * 아이디 중복 여부를 확인합니다.
     */
    public int countByUserId(String userId) {
        return mybatis.selectOne(NAMESPACE + ".countByUserId", userId);
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     */
    public int countByNickname(String nickname) {
        return mybatis.selectOne(NAMESPACE + ".countByNickname", nickname);
    }

    /**
     * 이메일 중복 여부를 확인합니다.
     */
    public int countByEmail(String email) {
        return mybatis.selectOne(NAMESPACE + ".countByEmail", email);
    }
    }
