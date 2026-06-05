package com.soso.domain.mypage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.mypage.dao.BusinessMypageDAO;
import com.soso.domain.mypage.dto.BusinessMypageDTO;

@Service
public class BusinessMypageService {

    @Autowired
    private BusinessMypageDAO businessMypageDAO;

    public BusinessMypageDTO getBusinessMypage(Long user_Seq) {
        BusinessMypageDTO dto = businessMypageDAO.getBusinessInfo(user_Seq);
        
        if (dto != null && dto.getProfileImageUrl() != null && !dto.getProfileImageUrl().isEmpty()) {
            // DB에 저장된 sysname을 풀 URL로 변환
            dto.setProfileImageUrl(dto.getProfileImageUrl());
        }
        
        return dto;
    }
}
