package com.soso.domain.mypage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.mypage.dto.BusinessMypageDTO;
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
}
