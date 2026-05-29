package com.soso.domain.member.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.soso.domain.member.dto.SignUpDto;
import com.soso.domain.member.services.MemberService;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    /**
     * 아이디 중복 체크 API
     */
    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam("userId") String userId) {
        logger.info("아이디 중복 체크 요청: {}", userId);
        boolean isDuplicated = memberService.isIdDuplicated(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);
        response.put("message", isDuplicated ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 닉네임 중복 체크 API
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam("nickname") String nickname) {
        logger.info("닉네임 중복 체크 요청: {}", nickname);
        boolean isDuplicated = memberService.isNicknameDuplicated(nickname);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);
        response.put("message", isDuplicated ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 이메일 중복 체크 API
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("email") String email) {
        logger.info("이메일 중복 체크 요청: {}", email);
        boolean isDuplicated = memberService.isEmailDuplicated(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);
        response.put("message", isDuplicated ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 사업자 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(
            @RequestPart("joinData") String joinData,
            @RequestPart(value = "exteriorImg", required = false) MultipartFile exteriorImg,
            @RequestPart(value = "interiorImg", required = false) MultipartFile interiorImg) {

        logger.info("회원가입 API 호출");
        Map<String, String> response = new HashMap<>();
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SignUpDto signUpDto = objectMapper.readValue(joinData, SignUpDto.class);

            memberService.signUp(signUpDto, exteriorImg, interiorImg);

            response.put("status", "success");
            response.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("회원가입 처리 중 오류 발생: {}", e.getMessage());
            // 상세한 에러 처리는 GlobalExceptionHandler에서 수행하겠지만, 
            // 여기서는 간단히 에러 메시지만 담아 반환 (필요 시 throw e 하여 Handler가 처리하게 할 수 있음)
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
