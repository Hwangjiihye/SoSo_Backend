package com.soso.domain.account.services;

import com.soso.domain.account.dao.AccountDAO;
import com.soso.domain.account.dto.AccountRelationRequestDto;
import com.soso.domain.account.dto.AccountRelationResponseDto;
import com.soso.domain.account.dto.AccountSearchResponseDto;
import com.soso.domain.account.dto.ItemResponseDto;
import com.soso.domain.account.dto.PartnerDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @file AccountService.java
 * @description 거래처 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    /**
     * 파트너사(거래처) 검색
     */
    public List<AccountSearchResponseDto> searchAccounts(String searchTerm) {
        return accountDAO.searchPartnerStores(searchTerm);
    }

    /**
     * 모든 파트너사(거래처) 조회 (필터 적용)
     */
    public List<AccountSearchResponseDto> getAllPartnerStores(String searchTerm, String city, String district) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);
        params.put("city", city);
        params.put("district", district);
        return accountDAO.getAllPartnerStores(params);
    }

    /**
     * 거래처 관계 등록
     */
    @Transactional
    public boolean registerAccount(AccountRelationRequestDto relationDto) {
        return accountDAO.insertPartnerRelation(relationDto) > 0;
    }

    /**
     * 등록된 거래처 목록 조회 (필터 적용)
     */
    public List<AccountRelationResponseDto> getRegisteredAccounts(int businessSeq, String searchTerm, String city, String district) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessSeq", businessSeq);
        params.put("searchTerm", searchTerm);
        params.put("city", city);
        params.put("district", district);
        return accountDAO.getPartnerRelationsByBusinessSeq(params);
    }

    /**
     * 거래처별 품목 목록 조회
     */
    public List<ItemResponseDto> getPartnerItems(int partnerSeq) {
        return accountDAO.getItemsByPartnerSeq(partnerSeq);
    }

    /**
     * 특정 거래처 상세 정보 조회
     */
    public PartnerDetailDto getPartnerDetail(int partnerSeq) {
        return accountDAO.getPartnerDetail(partnerSeq);
    }

    /**
     * 거래처 관계 삭제
     */
    @Transactional
    public boolean deleteAccount(int relationSeq) {
        return accountDAO.deletePartnerRelation(relationSeq) > 0;
    }

    /**
     * 유저의 첫 번째 매장 시퀀스 조회
     */
    public Integer getFirstStoreSeqByUserSeq(int userSeq) {
        return accountDAO.getFirstStoreSeqByUserSeq(userSeq);
    }
}
