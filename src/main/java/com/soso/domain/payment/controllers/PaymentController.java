package com.soso.domain.payment.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.payment.dto.AutoPaymentScheduleDTO;
import com.soso.domain.payment.dto.PaymentCardDTO;
import com.soso.domain.payment.dto.PaymentDTO;
import com.soso.domain.payment.services.PaymentService;

@RestController
@RequestMapping("/account")
public class PaymentController {
	
	@Autowired
	private PaymentService accountServ;
	
	// 계좌 등록
	@PostMapping("/accountSystem")
	public ResponseEntity<String> insertAccount(@RequestBody PaymentDTO dto) {
		
		accountServ.insertAccount(dto);
		
		return ResponseEntity.ok().build();
	}
	
	// 계좌 출력
	@GetMapping("/accountList")
	public ResponseEntity<List<PaymentDTO>> accountList(@RequestParam Long storeSeq) {
		List<PaymentDTO> list = accountServ.accountList(storeSeq);
		return ResponseEntity.ok(list);
	}
	
	// 계좌 삭제 X -> Update
	@DeleteMapping("/accountDel/{accountSeq}")
	public ResponseEntity<String> accountDel(@PathVariable Long accountSeq) {
		
		int result = accountServ.accountDel(accountSeq);
		
		if(result == 0) {
			return ResponseEntity.badRequest().body("삭제할 계좌가 없습니다.");
		}
		return ResponseEntity.ok("계좌가 삭제 되었습니다.");
	}
	
	// 자동이체 설정 등록
	@PostMapping("/autoPaymentSchedule")
	public ResponseEntity<String> autoPaymentSchedule(@RequestBody AutoPaymentScheduleDTO dto) {
	    accountServ.autoPaymentSchedule(dto);
	    return ResponseEntity.ok("자동이체 설정이 등록되었습니다.");
	}
	
	// 카드 등록
	@PostMapping("/cards")
	public ResponseEntity<String> insertCard(@RequestBody PaymentCardDTO dto) {
		accountServ.insertCard(dto);
		return ResponseEntity.ok("카드가 등록되었습니다");
	}
	
	// 카드 조회
	@GetMapping("/cards")
	public ResponseEntity<List<PaymentCardDTO>> selectCard(@RequestParam Long storeSeq) {
		return ResponseEntity.ok(accountServ.selectCard(storeSeq));
	}

}
