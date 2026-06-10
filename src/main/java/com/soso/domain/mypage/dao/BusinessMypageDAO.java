package com.soso.domain.mypage.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.mypage.dto.BusinessMultiProfileDTO;
import com.soso.domain.mypage.dto.BusinessMypageDTO;
import com.soso.domain.mypage.dto.BusinessUpdateDTO;

@Repository
public class BusinessMypageDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.domain.mypage.repository.BusinessMypageRepository";

    public BusinessMypageDTO getBusinessInfo(Long userSeq, Long storeSeq) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("user_seq", userSeq);
        params.put("storeSeq", storeSeq);
        return sqlSession.selectOne(NAMESPACE + ".getBusinessInfo", params);
    }

    /**
     * 🏪 특정 유저(사장님)가 소유한 모든 매장 목록 가져오기
     */
    public java.util.List<BusinessMypageDTO> getAllStores(Long userSeq) {
        return sqlSession.selectList(NAMESPACE + ".getAllStoresBySeq", userSeq);
    }

    public int updateUser(BusinessUpdateDTO updateDto) {
        return sqlSession.update(NAMESPACE + ".updateUser", updateDto);
    }

    public int updateStore(BusinessUpdateDTO updateDto) {
        return sqlSession.update(NAMESPACE + ".updateStore", updateDto);
    }

    /**
     * 📝 새로운 매장 정보 DB에 넣기
     * 
     * @param registerDto - 저장할 매장 정보가 담긴 객체
     * @return 성공적으로 저장된 행(Row)의 개수 (보통 1)
     */
    public int insertStore(BusinessMultiProfileDTO registerDto) {
        // "NAMESPACE"는 SQL 문이 저장된 XML 파일의 위치를 가리킵니다.
        // ".insertStore"는 해당 XML 안에서 실행할 특정 SQL ID입니다.
        return sqlSession.insert(NAMESPACE + ".insertStore", registerDto);
    }
}
