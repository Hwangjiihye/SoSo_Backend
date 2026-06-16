package com.soso.domain.account.controllers;

import com.soso.domain.account.dto.AccountRelationRequestDto;
import com.soso.domain.account.dto.AccountRelationResponseDto;
import com.soso.domain.account.dto.AccountSearchResponseDto;
import com.soso.domain.account.dto.ItemResponseDto;
import com.soso.domain.account.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @file AccountController.java
 * @description 거래처(파트너사) 관련 API를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * 거래처(PARTNER 타입 유저의 매장) 검색 API
     * @param searchTerm 업체명 또는 사업자 번호
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPartnerStores(@RequestParam("searchTerm") String searchTerm) {
        logger.info("거래처 검색 요청: {}", searchTerm);
        
        List<AccountSearchResponseDto> results = accountService.searchAccounts(searchTerm);
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("count", results.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 거래처(PARTNER 타입 유저의 매장) 조회 API
     */
    @GetMapping("/all-partners")
    public ResponseEntity<Map<String, Object>> getAllPartnerStores() {
        logger.info("모든 거래처 조회 요청");
        
        List<AccountSearchResponseDto> results = accountService.getAllPartnerStores();
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("count", results.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 거래처 관계 등록 API
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerAccount(@RequestBody AccountRelationRequestDto relationDto) {
        logger.info("거래처 관계 등록 요청: businessSeq={}, partnerSeq={}", 
                relationDto.getBusinessSeq(), relationDto.getPartnerSeq());
        
        boolean success = accountService.registerAccount(relationDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", success ? "success" : "fail");
        response.put("message", success ? "거래처가 성공적으로 등록되었습니다." : "거래처 등록에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 등록된 거래처 목록 조회 API
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getRegisteredAccounts(@RequestParam("businessSeq") int businessSeq) {
        logger.info("등록된 거래처 목록 조회 요청: businessSeq={}", businessSeq);
        
        List<AccountRelationResponseDto> results = accountService.getRegisteredAccounts(businessSeq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("count", results.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 거래처의 품목 목록 조회 API
     */
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getPartnerItems(@RequestParam("partnerSeq") int partnerSeq) {
        logger.info("거래처 품목 조회 요청: partnerSeq={}", partnerSeq);
        
        List<ItemResponseDto> results = accountService.getPartnerItems(partnerSeq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("count", results.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 거래처 관계 삭제 API
     */
    @DeleteMapping("/{relationSeq}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable("relationSeq") int relationSeq) {
        logger.info("거래처 관계 삭제 요청: relationSeq={}", relationSeq);
        
        boolean success = accountService.deleteAccount(relationSeq);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", success ? "success" : "fail");
        response.put("message", success ? "거래처가 성공적으로 삭제되었습니다." : "거래처 삭제에 실패했습니다.");
        
        return ResponseEntity.ok(response);
    }
}
