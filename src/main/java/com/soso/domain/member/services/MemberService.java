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
     * 사업자 회원가입 처리 로직 (GCS 업로드 연동)
     */
    @Transactional(rollbackFor = Exception.class)
    public void signUp(SignUpDto signUpDto, MultipartFile exteriorImg, MultipartFile interiorImg) throws Exception {
        logger.info("회원가입 프로세스 시작: userId={}", signUpDto.getUserId());

        // 1. 아이디 중복 체크
        if (isIdDuplicated(signUpDto.getUserId())) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }
        
        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        signUpDto.setPassword(encodedPassword);

        // 3. 개업일자 변환
        if (signUpDto.getOpenDate() != null && !signUpDto.getOpenDate().isEmpty()) {
            signUpDto.setFormattedOpenDate(LocalDate.parse(signUpDto.getOpenDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        // 4. 회원 기본 정보 DB 인서트 (MyBatis useGeneratedKeys로 인해 userSeq가 채워짐)
        int result = memberDAO.insertMember(signUpDto);
        if (result == 0) {
            throw new RuntimeException("회원가입 DB 저장 중 오류가 발생했습니다.");
        }

        // 5. 방금 가입한 유저 번호(Auto-Increment PK) 확보
        int generatedUserSeq = signUpDto.getUserSeq();

        // 6. GCS 업로드 호출 (외관 이미지, 내부 이미지)
        String extUrl = fileService.uploadToGcsAndGetUrl(exteriorImg, generatedUserSeq, "STORE_IMAGE");
        String intUrl = fileService.uploadToGcsAndGetUrl(interiorImg, generatedUserSeq, "STORE_IMAGE");

        // 7. URL 결합 및 DB 최종 반영
        String combinedUrl = extUrl + ";" + intUrl;
        signUpDto.setStoreImage(combinedUrl);
        
        // 가입된 레코드의 store_image 컬럼을 최종 업데이트하여 마무리
        memberDAO.updateStoreImage(signUpDto);

        logger.info("회원가입 성공: userId={}, userSeq={}", signUpDto.getUserId(), generatedUserSeq);
    }

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
