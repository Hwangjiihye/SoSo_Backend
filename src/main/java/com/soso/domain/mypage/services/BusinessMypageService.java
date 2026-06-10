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
import com.soso.domain.member.services.BizValidationService;
import com.soso.domain.mypage.dao.BusinessMypageDAO;
import com.soso.domain.mypage.dto.BusinessMultiProfileDTO;
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

    @Autowired
    private BizValidationService bizValidationService; // 사업자 인증 서비스 주입

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

    /**
     * 🏪 다중 매장 등록 비즈니스 로직
     * DB에 매장 정보를 저장하고, 업로드된 사진 파일들을 처리합니다.
     * 
     * @param registerDto - 매장 정보 데이터 가방
     * @param exteriorImg - 외부 사진 파일
     * @param interiorImg - 내부 사진 파일
     * @return 성공 시 "success", 실패 시 "fail"
     * @throws Exception - DB 저장 또는 파일 업로드 중 발생할 수 있는 예외
     */
    @Transactional(rollbackFor = Exception.class)
    public String registerMultiProfile(BusinessMultiProfileDTO registerDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        
        // 1. [사업자 번호 검증] 
        // 새로 만든 BusinessMultiProfileDTO를 BizValidationService에 통째로 넘겨 검증합니다.
        // 내부적으로 '중복 체크'를 건너뛰도록 설계되어 있어, 이미 등록된 사장님 번호라도 인증이 가능합니다.
        boolean isValid = bizValidationService.validateBusiness(registerDto);
        
        if (!isValid) {
            // 국세청 데이터와 일치하지 않는 경우 "invalidBizInfo"를 반환하여 프론트에서 알 수 있게 합니다.
            return "invalidBizInfo";
        }

        // 2. [매장 정보 저장] 검증이 통과되면 stores 테이블에 새로운 매장 정보를 Insert 합니다.
        // MyBatis의 useGeneratedKeys 설정을 통해 생성된 storeSeq가 registerDto에 자동으로 채워집니다.
        int result = businessMypageDAO.insertStore(registerDto);

        if (result > 0) {
            // DB 저장 후 생성된 매장 번호와 사장님 회원 번호를 가져옵니다.
            Long storeSeq = registerDto.getStoreSeq();
            Integer userSeq = registerDto.getUserSeq().intValue();

            // 3. [파일 업로드] 가게 외부 사진이 있다면 GCS(구글 클라우드)에 업로드하고 DB에 기록합니다.
            if (exteriorImg != null && !exteriorImg.isEmpty()) {
                fileService.uploadToGcsAndGetUrlWithBoardSeq(exteriorImg, userSeq, "STORE_IMAGE", storeSeq.intValue());
            }

            // 4. [파일 업로드] 가게 내부 사진이 있다면 동일한 방식으로 처리합니다.
            if (interiorImg != null && !interiorImg.isEmpty()) {
                fileService.uploadToGcsAndGetUrlWithBoardSeq(interiorImg, userSeq, "STORE_IMAGE", storeSeq.intValue());
            }

            return "success";
        }
        
        // 저장이 실패한 경우 (MyBatis result가 0인 경우)
        return "fail";
    }
}
