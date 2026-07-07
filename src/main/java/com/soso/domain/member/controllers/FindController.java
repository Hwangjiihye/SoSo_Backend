package com.soso.domain.member.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.member.dto.FindDTO;
import com.soso.domain.member.services.FindService;

@RestController
@RequestMapping("/find")
public class FindController {
	
	@Autowired
	private FindService FindServ;

	@PostMapping("/findId")
	public ResponseEntity<String> findId(@RequestBody FindDTO dto) {
		
		boolean result = FindServ.findId(dto);
		
		if(!result) {
			return ResponseEntity.badRequest().body("일치하는 정보가 없습니다.");
		}
		return ResponseEntity.ok("인증번호가 전송되었습니다.");
	}
	
	
	@PostMapping("/check-code")
	public ResponseEntity<String> checkCode(@RequestBody FindDTO dto) {


	    String result = FindServ.checkCode(dto);
	    
	    if(result == null) {
	        return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
	    }

	    return ResponseEntity.ok(result);
	}
	
	@PutMapping("/password/reset")
	public ResponseEntity<String> resetPassword(@RequestBody FindDTO dto) {

	    boolean result = FindServ.updatePassword(dto);

	    if (result) {
	        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
	    } else {
	        return ResponseEntity.badRequest().body("비밀번호 변경에 실패했습니다.");
	    }
	}
	
	@PostMapping("/findPw")
	public ResponseEntity<String> findPw(@RequestBody FindDTO dto) {
		
		boolean result = FindServ.findPw(dto);
		
		if(!result) {
			return ResponseEntity.badRequest().body("일치하는 정보가 없습니다.");
		}
		
		return ResponseEntity.ok("인증번호가 전송되었습니다.");
	}
	
	@PostMapping("/password/check-code")
	public ResponseEntity<String> checkPasswordCode(@RequestBody FindDTO dto) {
		
		
		boolean result = FindServ.checkCodeForPassword(dto);
		
		if(!result) {
			return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
		}
		
		return ResponseEntity.ok("인증이 완료되었습니다.");
	}
}
