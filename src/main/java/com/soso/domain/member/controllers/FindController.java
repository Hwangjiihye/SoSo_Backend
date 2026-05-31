package com.soso.domain.member.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

	    System.out.println("===== Controller 인증번호 확인 진입 =====");
	    System.out.println("email : " + dto.getEmail());
	    System.out.println("code : " + dto.getCode());

	    String result = FindServ.checkCode(dto);
	    
	    if(result == null) {
	        return ResponseEntity.badRequest().body("인증번호가 일치하지 않습니다.");
	    }

	    return ResponseEntity.ok(result);
	}
}
