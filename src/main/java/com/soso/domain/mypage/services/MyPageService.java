package com.soso.domain.mypage.services;

import com.soso.domain.file.dao.FileDAO;
import com.soso.domain.file.dto.FileSaveDto;
import com.soso.domain.file.services.FileService;
import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.mypage.dao.MyPageDAO;
import com.soso.domain.mypage.dto.PartnerProfileDTO;
import com.soso.domain.mypage.dto.PartnerUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private MyPageDAO myPageDAO;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileDAO fileDAO;

    @Autowired
    private MemberDAO memberDAO;

    public PartnerProfileDTO getPartnerProfile(Long user_seq) {
        return myPageDAO.getPartnerProfile(user_seq);
    }

    @Transactional(rollbackFor = Exception.class)
    public String updatePartnerProfile(PartnerUpdateDTO updateDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        Integer userSeq = updateDto.getUserSeq();

        // 1. 닉네임 중복 체크 (본인 제외)
        if (memberDAO.countByNicknameExcludingSelf(updateDto.getNickname(), userSeq) > 0) {
            return "duplNickname";
        }

        // 2. 이메일 중복 체크 (본인 제외)
        if (memberDAO.countByEmailExcludingSelf(updateDto.getEmail(), userSeq) > 0) {
        	return "duplEmail";
        }

        // 3. 사용자 및 상점 정보 업데이트
        myPageDAO.updateUser(updateDto);
        myPageDAO.updateStore(updateDto);

        // 2. 기존 이미지 목록 조회
        List<FileSaveDto> existingFiles = fileDAO.getFilesByUserAndCategory(userSeq, "STORE_IMAGE");

        // 3. 외관 사진 처리 (Index 0)
        if (exteriorImg != null && !exteriorImg.isEmpty()) {
            String oldSysName = (existingFiles.size() > 0) ? existingFiles.get(0).getSysname() : null;
            fileService.updateFile(exteriorImg, userSeq, "STORE_IMAGE", oldSysName);
        }

        // 4. 내부 사진 처리 (Index 1)
        if (interiorImg != null && !interiorImg.isEmpty()) {
            String oldSysName = (existingFiles.size() > 1) ? existingFiles.get(1).getSysname() : null;
            fileService.updateFile(interiorImg, userSeq, "STORE_IMAGE", oldSysName);
        }
        return "success";
    }
}
