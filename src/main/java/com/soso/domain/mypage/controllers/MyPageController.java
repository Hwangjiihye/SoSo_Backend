package com.soso.domain.mypage.controllers;

import com.soso.domain.mypage.dto.PartnerProfileDTO;
import com.soso.domain.mypage.services.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/partner")
public class MyPageController {

    @Autowired
    private MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<PartnerProfileDTO> getPartnerProfile(@RequestAttribute("user_seq") Long user_seq) {
        
        PartnerProfileDTO profile = myPageService.getPartnerProfile(user_seq);
        System.out.println(profile.getCreatedAt());
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
