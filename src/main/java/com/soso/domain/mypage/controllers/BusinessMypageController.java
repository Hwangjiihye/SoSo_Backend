package com.soso.domain.mypage.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.mypage.dto.BusinessMypageDTO;
import com.soso.domain.mypage.dto.BusinessUpdateDTO;
import com.soso.domain.mypage.services.BusinessMypageService;

@RestController
@RequestMapping("/api/member/business")
public class BusinessMypageController {

    @Autowired
    private BusinessMypageService businessMypageService;

    @GetMapping("/profile")
    public ResponseEntity<BusinessMypageDTO> getBusinessProfile(@RequestAttribute("user_seq") Long user_seq) {
        BusinessMypageDTO profile = businessMypageService.getBusinessMypage(user_seq);
        if (profile == null) {
        	System.out.println("null프로필 : "+profile);
            return ResponseEntity.notFound().build();
        }
        System.out.println("프로필 : "+profile.getCompanyName());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateBusinessProfile(
            @RequestAttribute("user_seq") Long userSeq,
            @ModelAttribute BusinessUpdateDTO updateDto) {

        Map<String, String> response = new HashMap<>();
        try {
            updateDto.setUserSeq(userSeq);
            System.out.println("컨트롤러 DTO : " + updateDto.getNickname() + " : " + updateDto.getEmail() + " : " + updateDto.getAddress1());
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
}
