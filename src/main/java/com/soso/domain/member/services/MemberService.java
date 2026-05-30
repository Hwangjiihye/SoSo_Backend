package com.soso.domain.member.services;

import com.soso.domain.member.dao.MemberDAO;
import com.soso.domain.member.dto.SignUpDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class MemberService {

    // SLF4J Logger 직접 생성 (Lombok 사용 금지 규정 준수)
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 파일 저장 경로 (서버 로컬)
    private final String uploadPath = "C:/soso_uploads/";

    /**
     * 사업자 회원가입 처리 로직
     */
    @Transactional
    public void signUp(SignUpDto signUpDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        logger.info("회원가입 프로세스 시작: userId={}", signUpDto.getUserId());

        // 1. 아이디 중복 체크 (방어적 확인)
        if (isIdDuplicated(signUpDto.getUserId())) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }
        
        // 2. 비밀번호 암호화 후 재설정
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(encodedPassword);

        // 3. 개업일자(String) -> LocalDate 변환
        if (signUpDto.getOpenDate() != null && !signUpDto.getOpenDate().isEmpty()) {
            signUpDto.setFormattedOpenDate(LocalDate.parse(signUpDto.getOpenDate(), DateTimeFormatter.ISO_DATE));
        }

        // 4. 파일 업로드 처리
        String extPath = saveFile(exteriorImg);
        String intPath = saveFile(interiorImg);

        // 5. 사진 경로 결합 (외부경로;내부경로)
        String combinedPath = extPath + ";" + intPath;
        signUpDto.setStoreImage(combinedPath);

        // 6. DB 저장
        int result = memberDAO.insertMember(signUpDto);
        if (result == 0) {
            logger.error("회원가입 DB 저장 실패: {}", signUpDto.getUserId());
            throw new RuntimeException("회원가입 DB 저장 중 오류가 발생했습니다.");
        }

        logger.info("회원가입 성공: userId={}", signUpDto.getUserId());
    }

    /**
     * 아이디 중복 확인
     */
    public boolean isIdDuplicated(String userId) {
        return memberDAO.countByUserId(userId) > 0;
    }

    /**
     * 닉네임 중복 확인
     */
    public boolean isNicknameDuplicated(String nickname) {
        return memberDAO.countByNickname(nickname) > 0;
    }

    /**
     * 이메일 중복 확인
     */
    public boolean isEmailDuplicated(String email) {
        return memberDAO.countByEmail(email) > 0;
    }

    /**
     * 파일을 물리적으로 저장하고 저장된 파일명을 반환합니다.
     */
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String uuidName = UUID.randomUUID().toString() + "_" + originalName;
        
        File targetFile = new File(uploadPath, uuidName);
        file.transferTo(targetFile);

        return uuidName;
    }
}
