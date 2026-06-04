package com.soso.domain.mypage.services;

import com.soso.domain.mypage.dao.MyPageDAO;
import com.soso.domain.mypage.dto.PartnerProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {

    @Autowired
    private MyPageDAO myPageDAO;

    public PartnerProfileDTO getPartnerProfile(Long user_seq) {
        return myPageDAO.getPartnerProfile(user_seq);
    }
}
