package com.soso.domain.account.services;

import com.soso.domain.account.dao.AccountDAO;
import com.soso.domain.account.dto.AccountRelationRequestDto;
import com.soso.domain.account.dto.AccountRelationResponseDto;
import com.soso.domain.account.dto.AccountSearchResponseDto;
import com.soso.domain.account.dto.ItemResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 거래처 관계 등록
     */
    @Transactional
    public boolean registerAccount(AccountRelationRequestDto relationDto) {
        return accountDAO.insertPartnerRelation(relationDto) > 0;
    }

    /**
     * 등록된 거래처 목록 조회
     */
    public List<AccountRelationResponseDto> getRegisteredAccounts(int businessSeq) {
        return accountDAO.getPartnerRelationsByBusinessSeq(businessSeq);
    }

    /**
     * 거래처별 품목 목록 조회
     */
    public List<ItemResponseDto> getPartnerItems(int partnerSeq) {
        return accountDAO.getItemsByPartnerSeq(partnerSeq);
    }

    /**
     * 거래처 관계 삭제
     */
    @Transactional
    public boolean deleteAccount(int relationSeq) {
        return accountDAO.deletePartnerRelation(relationSeq) > 0;
    }
}
