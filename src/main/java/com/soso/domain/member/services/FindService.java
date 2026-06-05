package com.soso.domain.member.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.soso.domain.member.dao.FindDAO;
import com.soso.domain.member.dto.FindDTO;

@Service
public class FindService {
	
	@Autowired
	private FindDAO FindDAO;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 이메일별 인증번호 임시 저장
	private Map<String, String> codeStorage = new HashMap<>();
	
	public boolean findId(FindDTO dto) {
		
		int count = FindDAO.findId(dto);
		
		if(count == 0) {
			return false;
		}
		
		// 랜덤 인증번호 생성
		String code = String.valueOf((int)(Math.random() * 900000) + 100000);
		
		// 인증번호 저장
		codeStorage.put(dto.getEmail(), code);
		
		// 메일 전송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(dto.getEmail());
		message.setSubject("[SoSo] 아이디 찾기 인증번호");
		message.setText("인증번호는 " + code + " 입니다.");

		mailSender.send(message);
		
		return true;
	}
	
	// 사용자가 인증번호를 입력했을 때 확인하는 메서드
	public String checkCode(FindDTO dto) {
		
		System.out.println("인증 확인 요청 이메일 : " + dto.getEmail());
		System.out.println("사용자가 입력한 인증번호 : " + dto.getCode());
		System.out.println("서버에 저장된 인증번호 : " + codeStorage.get(dto.getEmail()));
		System.out.println("전체 저장소 : " + codeStorage);
		
		String savedCode = codeStorage.get(dto.getEmail());
		
		if(savedCode == null) {
			return null;
		}
		
		if(!savedCode.equals(dto.getCode())) {
			return null;
		}
		
		// 인증 성공하면 아이디 조회
		String foundId = FindDAO.getIdByEmail(dto.getEmail());
		
		return foundId;
	}
	
	// 비밀번호 재설정
	public boolean updatePassword(FindDTO dto) {
	    
	// 1. 새 비밀번호 암호화
	   String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

	// 2. DTO에 암호화된 비밀번호 다시 넣기
	dto.setNewPassword(encodedPassword);

	// 3. DB update
	int result = FindDAO.updatePassword(dto);
	    
	// 4. 수정 성공 여부 반환
	return result > 0;
	}
	
	// 비밀번호 찾기 - 아이디 + 이메일 확인 후 인증번호 전송
	public boolean findPw(FindDTO dto) {
		
		int count = FindDAO.findPw(dto);
		
		if(count == 0) {
			return false;
		}
		
		// 랜덤 인증번호 생성
		String code = String.valueOf((int)(Math.random() * 900000) + 100000);
		
		// 인증번호 저장
		codeStorage.put(dto.getEmail(), code);
		
		// 메일 전송
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(dto.getEmail());
		message.setSubject("[SoSo] 비밀번호 찾기 인증번호");
		message.setText("인증번호는 " + code + " 입니다.");

		mailSender.send(message);
		
		return true;
	}
	
	// 비밀번호 찾기 - 인증번호 확인만 하는 메서드
	public boolean checkCodeForPassword(FindDTO dto) {
		
		System.out.println("비번찾기 인증 요청 이메일 : " + dto.getEmail());
		System.out.println("사용자가 입력한 인증번호 : " + dto.getCode());
		System.out.println("서버에 저장된 인증번호 : " + codeStorage.get(dto.getEmail()));
		System.out.println("전체 저장소 : " + codeStorage);
		
		String savedCode = codeStorage.get(dto.getEmail());
		
		if(savedCode == null) {
			return false;
		}
		
		if(!savedCode.equals(dto.getCode())) {
			return false;
		}
		
		return true;
	}

}
