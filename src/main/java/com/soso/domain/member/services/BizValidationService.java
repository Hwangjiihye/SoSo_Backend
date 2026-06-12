package com.soso.domain.member.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.member.dto.BizValidateDto;
import com.soso.domain.mypage.dto.BusinessMultiProfileDTO;

@Service
public class BizValidationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MemberDAO memberDao;
    
    @Value("${api.public-data.service-key}")
    private String serviceKey;

    // =========================================================================
    // [방식 1] 🔑 회원가입할 때 호출하는 메서드 (중복 체크 포함)
    // =========================================================================
    public boolean validateBusiness(String bNo, String startDt, String pNm, String bNm) {
        String cleanBNo = bNo.replaceAll("-", "");
        String cleanStartDt = startDt.replaceAll("-", "");
        
        // 회원가입 시에는 중복 체크를 'true'로 설정합니다.
        return executeNtsApi(cleanBNo, cleanStartDt, pNm, bNm, true);
    }

    // =========================================================================
    // [방식 2] 🏪 매장 추가/수정 시 호출하는 메서드 (중복 체크 제외)
    // =========================================================================
    public boolean validateBusiness(String bNo, String startDt, String pNm, String bNm, boolean isMultiProfile) {
        String cleanBNo = bNo.replaceAll("-", "");
        String cleanStartDt = startDt.replaceAll("-", "");
        
        // 매장 추가/수정 시(isMultiProfile=true)에는 중복 체크를 하지 않습니다.
        return executeNtsApi(cleanBNo, cleanStartDt, pNm, bNm, !isMultiProfile);
    }

    // =========================================================================
    // [방식 3] DTO 방식 (매장 추가 시 사용)
    // =========================================================================
    public boolean validateBusiness(BusinessMultiProfileDTO dto) {
        String cleanBNo = dto.getB_no().replaceAll("-", "");
        String cleanStartDt = dto.getStart_dt().replaceAll("-", "");
        
        // DTO 방식은 주로 매장 추가용이므로 중복 체크를 생략합니다.
        return executeNtsApi(cleanBNo, cleanStartDt, dto.getP_nm(), dto.getB_nm(), false);
    }

    // =========================================================================
    // [공통 핵심 로직]
    // =========================================================================
    private boolean executeNtsApi(String cleanBNo, String cleanStartDt, String pNm, String bNm, boolean checkDuplicate) {
        // 1. [중복 체크] 필요한 경우에만 수행
        if (checkDuplicate) {
            int count = memberDao.countByBizNo(cleanBNo);
            if (count > 0) {
                throw new IllegalArgumentException("DUPLICATED_BIZ_NO"); 
            }
        }
        
        // 2. 국세청 API URL 조립
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.odcloud.kr/api/nts-businessman/v1/validate")
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BizValidateDto.Request.BizInfo bizInfo = new BizValidateDto.Request.BizInfo(
                cleanBNo, cleanStartDt, pNm, bNm, "", "", ""
        );

        List<BizValidateDto.Request.BizInfo> list = new ArrayList<>();
        list.add(bizInfo);
        
        BizValidateDto.Request requestBody = new BizValidateDto.Request(list);
        HttpEntity<BizValidateDto.Request> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            ResponseEntity<BizValidateDto.Response> response = 
                    restTemplate.postForEntity(uri, entity, BizValidateDto.Response.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BizValidateDto.Response resBody = response.getBody();
                if (resBody.getData() != null && !resBody.getData().isEmpty()) {
                    String validCode = resBody.getData().get(0).getValid();
                    return "01".equals(validCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}