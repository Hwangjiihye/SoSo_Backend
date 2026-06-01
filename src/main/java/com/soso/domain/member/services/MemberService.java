package com.soso.domain.member.services;

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
     * [완벽한 가입 프로세스 시퀀스]
     * 1. users 인서트 -> 2. stores 초기 인서트 -> 3. GCS 파일 업로드 -> 4. URL 결합 및 DB 반영
     */
    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpDto signUpDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        logger.info("회원가입 시퀀스 시작: userId={}", signUpDto.getUserId());

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

        // 4. 확보된 userSeq 획득
        int generatedUserSeq = signUpDto.getUserSeq();
        logger.info("생성된 user_seq: {}", generatedUserSeq);

        // 5. [stores] 테이블 릴레이 인서트 (초기 정보 저장)
        int storeResult = memberDAO.insertStore(signUpDto);
        if (storeResult == 0) {
            throw new RuntimeException("매장 기초 정보 저장 실패");
        }

        // 6. GCS 업로드 호출 (확보한 generatedUserSeq 연동)
        String extUrl = fileService.uploadToGcsAndGetUrl(exteriorImg, generatedUserSeq, "STORE_IMAGE");
        String intUrl = fileService.uploadToGcsAndGetUrl(interiorImg, generatedUserSeq, "STORE_IMAGE");

        // 7. 업로드된 URL 결합 (세미콜론 구분)
        String combinedUrl = (extUrl == null ? "" : extUrl) + ";" + (intUrl == null ? "" : intUrl);
        signUpDto.setStoreImage(combinedUrl);

        // 8. 데이터베이스 store_image 컬럼 최종 업데이트
        memberDAO.updateStoreImage(signUpDto);

        logger.info("회원가입 프로세스 완료: userSeq={}", generatedUserSeq);
    }

    /**
     * 회원 탈퇴 (데이터 무결성을 위한 역순 삭제)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(int userSeq) {
        logger.info("회원 탈퇴 요청: userSeq={}", userSeq);
        
        // 1. 자식 테이블(stores) 먼저 삭제 (고아 데이터 방지)
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
