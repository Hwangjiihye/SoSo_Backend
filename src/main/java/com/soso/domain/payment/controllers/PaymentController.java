package com.soso.domain.payment.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.soso.domain.payment.dto.OrderPaymentRequestDTO;
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
	
	// 발주 결제 요청
	// 프론트에서 "등록된 카드로 결제" 버튼을 눌렀을 때 호출됨
	@PostMapping("/order/pay")
	public ResponseEntity<Map<String, Object>> payOrders(@RequestBody OrderPaymentRequestDTO dto) {

	    // 실제 결제 로직은 Service에서 처리
	    // Controller는 요청을 받고 Service로 넘기는 역할만 함
	    Map<String, Object> result = accountServ.payOrders(dto);

	    // Service에서 만든 결과를 프론트로 반환
	    return ResponseEntity.ok(result);
	}
	

	// 이체관리 최근 결제 내역 조회
	// 카드 결제 성공 후 payments 테이블에 저장된 내역을
	// 검색 조건에 맞게 조회해서 화면에 보여주기 위한 API
	@GetMapping("/recent-payments")
	public ResponseEntity<List<Map<String, Object>>> selectRecentPayments(
	        @RequestParam Integer storeSeq,
	        @RequestParam(required = false, defaultValue = "week") String period,
	        @RequestParam(required = false) String startDate,
	        @RequestParam(required = false) String endDate,
	        @RequestParam(required = false) String keyword) {

	    // Service에 검색 조건을 전달해서 결제 내역 조회
	    List<Map<String, Object>> list = accountServ.selectRecentPayments(
	            storeSeq,
	            period,
	            startDate,
	            endDate,
	            keyword
	    );

	    // 조회 결과를 프론트로 반환
	    return ResponseEntity.ok(list);
	}
	
	// 거래처 로그인 기준 수금관리 대시보드 조회
	@GetMapping("/collection")
	public ResponseEntity<Map<String, Object>> selectCollectionDashboard(
	        @RequestParam Long storeSeq) {

	    Map<String, Object> result = accountServ.selectCollectionDashboard(storeSeq);

	    return ResponseEntity.ok(result);
	}
	
	// 카드 삭제 X -> is_active = 'N' 처리
	@DeleteMapping("/cards/{cardSeq}")
	public ResponseEntity<String> deleteCard(@PathVariable Long cardSeq) {

	    int result = accountServ.deleteCard(cardSeq);

	    if (result == 0) {
	        return ResponseEntity.badRequest().body("삭제할 카드가 없습니다.");
	    }

	    return ResponseEntity.ok("카드가 삭제되었습니다.");
	}

}
