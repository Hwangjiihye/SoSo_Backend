package com.soso.domain.member.services;

import com.soso.domain.file.dto.FileSaveDto;
import com.soso.domain.file.services.FileService;
import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.member.dto.SignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 아키텍처 규칙을 준수한 회원 서비스
 * 필드 주입 방식 및 순수 자바 스타일 적용
 * 공통 파일 테이블(files) 연동 로직 포함
 */
@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private FileService fileService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * [리팩토링된 회원가입 시퀀스]
     * 1. users 인서트 -> 2. user_seq 확보 -> 3. stores 인서트 -> 4. 파일 업로드 및 개별 Files 인서트
     */
    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpDto signUpDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        logger.info("회원가입 프로세스 시작: userId={}", signUpDto.getUserId());

        // 1. 아이디 중복 체크
        if (memberDAO.countByUserId(signUpDto.getUserId()) > 0) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        // 2. 비밀번호 암호화 및 날짜 변환
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        if (signUpDto.getOpenDate() != null && !signUpDto.getOpenDate().isEmpty()) {
            signUpDto.setFormattedOpenDate(LocalDate.parse(signUpDto.getOpenDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        // 3. [users] 테이블 인서트 (useGeneratedKeys로 user_seq 확보)
        int userResult = memberDAO.insertUser(signUpDto);
        if (userResult == 0) {
            throw new RuntimeException("계정 정보 저장 실패");
        }

        // 4. MyBatis useGeneratedKeys를 통해 생성된 user_seq 확보
        int generatedUserSeq = signUpDto.getUserSeq();
        logger.info("확보된 user_seq: {}", generatedUserSeq);

        // 5. [stores] 테이블 인서트 (store_image 컬럼 미참조)
        int storeResult = memberDAO.insertStore(signUpDto);
        if (storeResult == 0) {
            throw new RuntimeException("매장 정보 저장 실패");
        }

        // 6. [files] 이미지 처리 - 외관 사진
        processImageUpload(exteriorImg, generatedUserSeq, "exterior_image");

        // 7. [files] 이미지 처리 - 내부 사진
        processImageUpload(interiorImg, generatedUserSeq, "interior_image");

        logger.info("회원가입 및 매장/파일 등록 완료: userSeq={}", generatedUserSeq);
    }

    /**
     * 이미지 업로드 및 files 테이블 레코드 생성을 처리하는 공통 프라이빗 메서드
     */
    private void processImageUpload(MultipartFile file, int userSeq, String typePrefix) throws Exception {
        if (file != null && !file.isEmpty()) {
            // GCS 업로드 및 URL 반환
            String gcsUrl = fileService.uploadToGcsAndGetUrl(file, userSeq, "STORE_IMAGE");

            logger.info("파일 등록 완료: type={}, url={}", typePrefix, gcsUrl);
        }
    }

    /**
     * 회원 탈퇴 (데이터 무결성을 위한 역순 삭제)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(int userSeq) {
        logger.info("회원 탈퇴 요청: userSeq={}", userSeq);
        
        // 1. 자식 테이블(stores) 먼저 삭제
        memberDAO.deleteStoresByKey(userSeq);
        
        // 2. 부모 테이블(users) 삭제
        int result = memberDAO.deleteUser(userSeq);
        if (result == 0) {
            throw new RuntimeException("회원 정보 삭제 실패 (대상 없음)");
        }
        
        logger.info("회원 탈퇴 처리 완료: userSeq={}", userSeq);
    }

    // 중복 체크 편의 메서드
    public boolean isIdDuplicated(String userId) {
        return memberDAO.countByUserId(userId) > 0;
    }

    public boolean isNicknameDuplicated(String nickname) {
        return memberDAO.countByNickname(nickname) > 0;
    }

    public boolean isEmailDuplicated(String email) {
        return memberDAO.countByEmail(email) > 0;
    }
}
