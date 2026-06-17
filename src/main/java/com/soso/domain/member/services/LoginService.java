package com.soso.domain.member.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soso.domain.member.dao.LoginDAO;
import com.soso.domain.member.dto.LoginDTO;

@Service
public class LoginService {
	
	@Autowired
	private LoginDAO LoginDAO;
	
	@Autowired
	private PasswordEncoder PasswordEncoder;
	
	public Map<String, Object> toLogin(LoginDTO dto) {
		
		// 1. 아이디 + 회원유형으로 회원 조회
		Map<String, Object> member = LoginDAO.toLogin(dto);
		
		Map<String, Object> resultMap = new HashMap<>();
		// 2. 아이디 또는 회원 유형이 안맞으면 실패
		if(member == null) {
			return null;
		}
		
		// 3. DB에 저장된 암호화 비밀번호 가져오기
		String DBPassword = (String)member.get("password");
		
		// [추가] 탈퇴 회원 여부 확인
		String status = (String)member.get("status");
		if ("WITHDRAWN".equals(status)) {
			// 🎯 자네가 의도한 "isWithDraw" 응답 규격을 가방에 예쁘게 패킹!
			resultMap.put("status", "isWithDraw");
			resultMap.put("message", "탈퇴 처리된 계정입니다. 해당 계정으로는 로그인할 수 없습니다.");
			return resultMap; // 비밀번호 검사까지 갈 것도 없이 여기서 즉시 리턴(차단)!
		}
		
		// 4. 입력한 비밀번호와 DB 암호화 비밀번호 비교
		boolean isMatch = PasswordEncoder.matches(dto.getPw(), DBPassword);
		
		// 5. 비밀번호가 틀리면 실패
		if(!isMatch) {
			return null;
		}
		
		// 6. 전부 맞으면 회원 정보 반환
		return member;
	}
	public List<Integer> getStoreListByUserSeq(Long userSeq) {
	    return LoginDAO.getStoreListByUserSeq(userSeq);
	}
}
