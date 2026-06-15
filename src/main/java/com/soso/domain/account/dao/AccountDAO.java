package com.soso.domain.account.dao;

import com.soso.domain.account.dto.AccountRelationRequestDto;
import com.soso.domain.account.dto.AccountRelationResponseDto;
import com.soso.domain.account.dto.AccountSearchResponseDto;
import com.soso.domain.account.dto.ItemResponseDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @file AccountDAO.java
 * @description 거래처 관련 DB 접근을 담당하는 DAO 클래스입니다.
 */
@Repository
public class AccountDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    private static final String NAMESPACE = "com.soso.domain.account.dao.AccountDAO";

    /**
     * 업체명 또는 사업자 번호로 PARTNER 타입 회원의 매장 정보를 조회합니다.
     */
    public List<AccountSearchResponseDto> searchPartnerStores(String searchTerm) {
        return mybatis.selectList(NAMESPACE + ".searchPartnerStores", searchTerm);
    }

    /**
     * 거래처 관계 정보를 저장합니다.
     */
    public int insertPartnerRelation(AccountRelationRequestDto relationDto) {
        return mybatis.insert(NAMESPACE + ".insertPartnerRelation", relationDto);
    }

    /**
     * 특정 사업장(소상공인)의 등록된 거래처 목록을 조회합니다.
     */
    public List<AccountRelationResponseDto> getPartnerRelationsByBusinessSeq(int businessSeq) {
        return mybatis.selectList(NAMESPACE + ".getPartnerRelationsByBusinessSeq", businessSeq);
    }

    /**
     * 특정 거래처(PARTNER)가 보유한 품목 목록을 조회합니다.
     */
    public List<ItemResponseDto> getItemsByPartnerSeq(int partnerSeq) {
        return mybatis.selectList(NAMESPACE + ".getItemsByPartnerSeq", partnerSeq);
    }

    /**
     * 거래처 관계 정보를 삭제합니다.
     */
    public int deletePartnerRelation(int relationSeq) {
        return mybatis.delete(NAMESPACE + ".deletePartnerRelation", relationSeq);
    }
}
