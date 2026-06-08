package com.soso.domain.mypage.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.soso.domain.file.dao.FileDAO;
import com.soso.domain.file.dto.FileSaveDto;
import com.soso.domain.file.services.FileService;
import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.mypage.dao.BusinessMypageDAO;
import com.soso.domain.mypage.dto.BusinessMypageDTO;
import com.soso.domain.mypage.dto.BusinessUpdateDTO;

@Service
public class BusinessMypageService {

    @Autowired
    private BusinessMypageDAO businessMypageDAO;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDAO fileDAO;

    public BusinessMypageDTO getBusinessMypage(Long user_Seq) {
        BusinessMypageDTO dto = businessMypageDAO.getBusinessInfo(user_Seq);
        
        if (dto != null && dto.getProfileImageUrl() != null && !dto.getProfileImageUrl().isEmpty()) {
            // DB에 저장된 sysname을 풀 URL로 변환
            dto.setProfileImageUrl(dto.getProfileImageUrl());
        }
        
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateBusinessProfile(BusinessUpdateDTO updateDto) throws Exception {
        Long userSeq = updateDto.getUserSeq();

        // 1. 닉네임 중복 체크 (본인 제외)
        if (memberDAO.countByNicknameExcludingSelf(updateDto.getNickname(), userSeq.intValue()) > 0) {
            return "duplNickname";
        }

        // 2. 이메일 중복 체크 (본인 제외)
        if (memberDAO.countByEmailExcludingSelf(updateDto.getEmail(), userSeq.intValue()) > 0) {
            return "duplEmail";
        }

        // 3. 사용자 및 상점 정보 업데이트
        businessMypageDAO.updateUser(updateDto);
        businessMypageDAO.updateStore(updateDto);

        // 4. 기존 이미지 목록 조회
        List<FileSaveDto> existingFiles = fileDAO.getFilesByUserAndCategory(userSeq.intValue(), "STORE_IMAGE");

        // 5. 외관 사진 처리 (Index 0)
        MultipartFile exteriorImg = updateDto.getExteriorImg();
        if (exteriorImg != null && !exteriorImg.isEmpty()) {
            String oldSysName = (existingFiles.size() > 0) ? existingFiles.get(0).getSysname() : null;
            fileService.updateFile(exteriorImg, userSeq.intValue(), "STORE_IMAGE", oldSysName);
        }

        // 6. 내부 사진 처리 (Index 1)
        MultipartFile interiorImg = updateDto.getInteriorImg();
        if (interiorImg != null && !interiorImg.isEmpty()) {
            String oldSysName = (existingFiles.size() > 1) ? existingFiles.get(1).getSysname() : null;
            fileService.updateFile(interiorImg, userSeq.intValue(), "STORE_IMAGE", oldSysName);
        }

        return "success";
    }
}
