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

@Service
public class BizValidationService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private MemberDAO memberDao;
	
    @Value("${api.public-data.service-key}")
    private String serviceKey;

    public boolean validateBusiness(String bNo, String startDt, String pNm, String bNm) {
        
    	String cleanBNo = bNo.replaceAll("-", "");
        
        // 2. 🔥 [실무 치트키] 외부 API 쏘기 전에 우리 DB 먼저 검사!
        int count = memberDao.countByBizNo(cleanBNo);
        if (count > 0) {
            // 우리 DB에 이미 존재한다면 국세청 찌르지도 않고 바로 탈락!
            // 여기서 예외를 던지거나, 프론트가 알아먹을 특수한 문자열을 리턴하네.
            throw new IllegalArgumentException("DUPLICATED_BIZ_NO"); 
        }
    	
        // 1. URL 빌드 (공공데이터포털 특유의 서비스키 중복 인코딩 방지 로직)
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.odcloud.kr/api/nts-businessman/v1/validate")
                .queryParam("serviceKey", serviceKey)
                .build(true) // 서비스키가 이미 인코딩된 상태로 장부에 적혀있을 때를 대비한 안전장치일세
                .toUri();

        // 2. 헤더 및 데이터 정제
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

      
        String cleanStartDt = startDt.replaceAll("-", "");

        // 3. 통합 DTO의 Request 이너 클래스 사용
        // 💡 BizValidateDto.Request.BizInfo 형태로 계층 구조를 명확히 찔러넣네
        BizValidateDto.Request.BizInfo bizInfo = new BizValidateDto.Request.BizInfo(
                cleanBNo, 
                cleanStartDt, 
                pNm, 
                bNm, 
                "", "", ""
        );

        List<BizValidateDto.Request.BizInfo> list = new ArrayList<>();
        list.add(bizInfo);
        
        BizValidateDto.Request requestBody = new BizValidateDto.Request(list);

        // 4. API 호출 (응답 타입도 BizValidateDto.Response 클래스로 지정)
        HttpEntity<BizValidateDto.Request> entity = new HttpEntity<>(requestBody, headers);
        
        try {
            System.out.println("====== 국세청 API 호출 시작 (통합 DTO 아키텍처) ======");
            ResponseEntity<BizValidateDto.Response> response = 
                restTemplate.postForEntity(uri, entity, BizValidateDto.Response.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BizValidateDto.Response resBody = response.getBody();
                
                System.out.println("API 통신 성공! 상태: " + resBody.getStatus_code());

                if (resBody.getData() != null && !resBody.getData().isEmpty()) {
                    // 결과 코드 추출 (01: 유효함)
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