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

import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.member.dto.BizValidateDto;
import com.soso.domain.mypage.dto.BusinessMultiProfileDTO; // 👈 멀티프로필 DTO 임포트

@Service
public class BizValidationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MemberDAO memberDao;
    
    @Value("${api.public-data.service-key}")
    private String serviceKey;

    // =========================================================================
    // [방식 1] 🔑 회원가입할 때 호출하는 메서드 (기존 파라미터 방식 그대로 유지)
    // =========================================================================
    public boolean validateBusiness(String bNo, String startDt, String pNm, String bNm) {
        String cleanBNo = bNo.replaceAll("-", "");
        String cleanStartDt = startDt.replaceAll("-", "");
        
        System.out.println("[회원가입 검증] 👉 사업자번호: " + cleanBNo + " | 대표자: " + pNm);
        
        // 국세청 찌르기 전 공통 로직 실행 및 API 호출
        return executeNtsApi(cleanBNo, cleanStartDt, pNm);
    }

    // =========================================================================
    // [방식 2] 🏪 매장 추가(멀티프로필)할 때 호출하는 메서드 (새로운 DTO 방식 추가)
    // =========================================================================
    public boolean validateBusiness(BusinessMultiProfileDTO dto) {
        String cleanBNo = dto.getB_no().replaceAll("-", "");
        String cleanStartDt = dto.getStart_dt().replaceAll("-", "");
        String pNm = dto.getP_nm();
        
        System.out.println("[멀티프로필 검증] 👉 사업자번호: " + cleanBNo + " | 대표자: " + pNm);
        
        // 국세청 찌르기 전 공통 로직 실행 및 API 호출
        return executeNtsApi(cleanBNo, cleanStartDt, pNm);
    }

    // =========================================================================
    // [공통 핵심 로직] 중복 검사 및 실제 국세청 API 통신을 전담하는 내부 메서드
    // =========================================================================
    private boolean executeNtsApi(String cleanBNo, String cleanStartDt, String pNm) {
        // 1. 우리 DB 먼저 검사!
        int count = memberDao.countByBizNo(cleanBNo);
        if (count > 0) {
            throw new IllegalArgumentException("DUPLICATED_BIZ_NO"); 
        }
        
        // 2. 서비스키 인코딩 방지 URL 조립
        String urlStr = "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=" + serviceKey;
        URI uri = URI.create(urlStr);

        // 3. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. 국세청 전송용 내장 DTO(BizValidateDto) 조립 
        // 💡 4번째 상호명 자리는 공통적으로 ""(빈 문자열)을 던져서 무조건 패스시킵니다!
        BizValidateDto.Request.BizInfo bizInfo = new BizValidateDto.Request.BizInfo(
                cleanBNo, cleanStartDt, pNm, "", "", "", ""
        );

        List<BizValidateDto.Request.BizInfo> list = new ArrayList<>();
        list.add(bizInfo);
        
        BizValidateDto.Request requestBody = new BizValidateDto.Request(list);
        HttpEntity<BizValidateDto.Request> entity = new HttpEntity<>(requestBody, headers);
        
        // 5. 국세청 API 실제 호출
        try {
            System.out.println("====== 국세청 공통 API 호출 시작 ======");
            ResponseEntity<BizValidateDto.Response> response = 
                    restTemplate.postForEntity(uri, entity, BizValidateDto.Response.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BizValidateDto.Response resBody = response.getBody();
                System.out.println("API 통신 성공! 상태: " + resBody.getStatus_code());

                if (resBody.getData() != null && !resBody.getData().isEmpty()) {
                    String validCode = resBody.getData().get(0).getValid();
                    System.out.println("사업자 검증 최종 결과: " + resBody.getData().get(0).getValid_msg());
                    
                    return "01".equals(validCode);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ 국세청 API 통신 중 기술적 오류 발생!");
            e.printStackTrace();
        }

        return false;
    }
}