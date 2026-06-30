package com.soso.domain.mypage.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.soso.domain.mypage.dto.BusinessMultiProfileDTO;
import com.soso.domain.mypage.dto.BusinessMypageDTO;
import com.soso.domain.mypage.dto.BusinessUpdateDTO;
import com.soso.domain.mypage.dto.BusinessNotificationSettingsDTO;
import com.soso.domain.mypage.services.BusinessMypageService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/member/business")
public class BusinessMypageController {

    @Autowired
    private BusinessMypageService businessMypageService;

    @GetMapping("/profile")
    public ResponseEntity<BusinessMypageDTO> getBusinessProfile(
            @RequestAttribute("user_seq") Long user_seq,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long storeSeq) {
        
        // 특정 매장 번호(storeSeq)가 넘어오면 해당 매장 정보를, 없으면 기본 정보를 조회합니다.
        BusinessMypageDTO profile = businessMypageService.getBusinessMypage(user_seq, storeSeq);
        
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }

    /**
     * 🏪 사장님이 등록한 모든 매장 목록 조회 API
     * 헤더의 프로필 스위처(Profile Switcher) 등에 사용됩니다.
     */
    @GetMapping("/stores")
    public ResponseEntity<java.util.List<BusinessMypageDTO>> getAllStores(@RequestAttribute("user_seq") Long user_seq) {
        java.util.List<BusinessMypageDTO> stores = businessMypageService.getAllStores(user_seq);
        return ResponseEntity.ok(stores);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateBusinessProfile(
            @RequestAttribute("user_seq") Long userSeq,
            @ModelAttribute BusinessUpdateDTO updateDto) {

        Map<String, String> response = new HashMap<>();
        try {
            updateDto.setUserSeq(userSeq);
            String result = businessMypageService.updateBusinessProfile(updateDto);

            if ("duplNickname".equals(result)) {
                response.put("status", "duplNickname");
                response.put("message", "이미 사용 중인 닉네임입니다. 다시 입력해주세요");
            } else if ("duplEmail".equals(result)) {
                response.put("status", "duplEmail");
                response.put("message", "이미 사용 중인 이메일입니다. 다시 입력해주세요");
            } else if ("success".equals(result)) {
                response.put("status", "success");
                response.put("message", "업체 정보 수정이 완료되었습니다.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 🏪 다중 매장 등록 API
     * 프론트엔드에서 보낸 매장 정보와 이미지 파일들을 받아서 저장합니다.
     * 
     * @param userSeq - 현재 로그인한 사용자의 번호 (Interceptor나 Filter에서 설정됨)
     * @param registerData - JSON 형태의 매장 정보 문자열
     * @param exteriorImg - 가게 외부 사진 파일 (선택)
     * @param interiorImg - 가게 내부 사진 파일 (선택)
     * @return 등록 성공/실패 여부와 메시지
     */
    @PostMapping("/multiprofile/register")
    public ResponseEntity<Map<String, String>> registerMultiProfile(
            @RequestAttribute("user_seq") Long userSeq,
            @RequestPart("registerData") String registerData,
            @RequestPart(value = "exteriorImg", required = false) MultipartFile exteriorImg,
            @RequestPart(value = "interiorImg", required = false) MultipartFile interiorImg) {

        Map<String, String> response = new HashMap<>();
        try {
            // 1. JSON 문자열로 들어온 매장 정보를 DTO 객체로 변환합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            BusinessMultiProfileDTO registerDto = objectMapper.readValue(registerData, BusinessMultiProfileDTO.class);
            
            // 2. 현재 로그인한 사장님의 번호를 DTO에 저장합니다.
            registerDto.setUserSeq(userSeq);

            // 3. 서비스(Service) 레이어에 등록 처리를 요청합니다.
            String result = businessMypageService.registerMultiProfile(registerDto, exteriorImg, interiorImg);

            // 4. 처리 결과에 따라 응답 메시지를 작성합니다.
            if ("success".equals(result)) {
                response.put("status", "success");
                response.put("message", "새로운 매장이 성공적으로 등록되었습니다.");
            } else if ("invalidBizInfo".equals(result)) {
                response.put("status", "error");
                response.put("message", "사업자 정보가 유효하지 않거나 이미 등록된 번호입니다.");
            } else {
                response.put("status", "error");
                response.put("message", "매장 등록에 실패했습니다.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 오류 발생 시 로그를 찍고 에러 메시지를 반환합니다.
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 스마트 알림 설정 조회 API
     * GET /api/member/business/notification-settings
     */
    @GetMapping("/notification-settings")
    public ResponseEntity<BusinessNotificationSettingsDTO> getNotificationSettings(
            @RequestAttribute("user_seq") Long userSeq,
            @RequestParam Long storeSeq) {
        
        BusinessNotificationSettingsDTO settings = businessMypageService.getNotificationSettings(userSeq, storeSeq);
        return ResponseEntity.ok(settings);
    }

    /**
     * 스마트 알림 설정 저장 API
     * PUT /api/member/business/notification-settings
     */
    @PutMapping("/notification-settings")
    public ResponseEntity<Map<String, String>> updateNotificationSettings(
            @RequestAttribute("user_seq") Long userSeq,
            @RequestParam Long storeSeq,
            @RequestBody BusinessNotificationSettingsDTO settingsDto) {
        
        Map<String, String> response = new HashMap<>();
        try {
            businessMypageService.updateNotificationSettings(userSeq, storeSeq, settingsDto);
            response.put("status", "success");
            response.put("message", "알림 설정이 성공적으로 저장되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "알림 설정 저장 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
