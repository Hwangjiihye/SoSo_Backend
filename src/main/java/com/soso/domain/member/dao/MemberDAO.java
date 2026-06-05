package com.soso.domain.member.dao;

import com.soso.domain.member.dto.SignUpDto;
import com.soso.domain.file.dto.FileSaveDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 필드 주입 방식을 사용한 순수 자바 스타일의 MemberDAO
 * 공통 파일 테이블(files) 연동 로직 포함
 */
@Repository
public class MemberDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    private static final String NAMESPACE = "com.soso.domain.member.dao.MemberDAO";

    /**
     * users 테이블에 계정 정보를 인서트합니다. (useGeneratedKeys 적용)
     */
    public int insertUser(SignUpDto signUpDto) {
        return mybatis.insert(NAMESPACE + ".insertUser", signUpDto);
    }

    /**
     * stores 테이블에 매장 상세 정보를 인서트합니다. (store_image 컬럼 제외)
     */
    public int insertStore(SignUpDto signUpDto) {
        return mybatis.insert(NAMESPACE + ".insertStore", signUpDto);
    }

  
    /**
     * 회원 탈퇴 시 매장 정보들을 먼저 삭제합니다.
     */
    public int deleteStoresByKey(int userSeq) {
        return mybatis.delete(NAMESPACE + ".deleteStoresByKey", userSeq);
    }

    /**
     * 회원 탈퇴 시 계정 정보를 삭제합니다.
     */
    public int deleteUser(int userSeq) {
        return mybatis.delete(NAMESPACE + ".deleteUser", userSeq);
    }

    // 중복 체크 쿼리
    public int countByUserId(String userId) {
        return mybatis.selectOne(NAMESPACE + ".countByUserId", userId);
    }

    public int countByNickname(String nickname) {
        return mybatis.selectOne(NAMESPACE + ".countByNickname", nickname);
    }

    public int countByEmail(String email) {
        return mybatis.selectOne(NAMESPACE + ".countByEmail", email);
    }

    public int countByBizNo(String bizNo) {
        return mybatis.selectOne(NAMESPACE + ".countByBizNo", bizNo);
    }

    public int countByNicknameExcludingSelf(String nickname, int userSeq) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("nickname", nickname);
        params.put("userSeq", userSeq);
        return mybatis.selectOne(NAMESPACE + ".countByNicknameExcludingSelf", params);
    }

    public int countByEmailExcludingSelf(String email, int userSeq) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("email", email);
        params.put("userSeq", userSeq);
        return mybatis.selectOne(NAMESPACE + ".countByEmailExcludingSelf", params);
    }

    /**
     * userSeq를 통해 암호화된 기존 비밀번호를 조회합니다.
     */
    public String getPasswordByUserSeq(Long userSeq) {
        return mybatis.selectOne(NAMESPACE + ".getPasswordByUserSeq", userSeq);
    }

    /**
     * 비밀번호를 업데이트합니다.
     */
    public int updatePassword(Long userSeq, String encodedPassword) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        System.out.println(encodedPassword);
        params.put("userSeq", userSeq);
        params.put("encodedPassword", encodedPassword);
        return mybatis.update(NAMESPACE + ".updatePassword", params);
    }
}
