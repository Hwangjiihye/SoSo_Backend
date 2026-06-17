package com.soso.domain.order.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.order.dto.OrderDTO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderListDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;
import com.soso.domain.order.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService OrderServ;
	
	// 사업자 재고 비교
	@GetMapping("/check")
	public ResponseEntity <List<OrderRecommendDTO>> recommendStock(@RequestParam("itemName") String itemName, HttpServletRequest request) {
		
//		String loginId = (String)request.getAttribute("loginId");
		
		Long user_seq = (Long)request.getAttribute("user_seq");
		
		List<OrderRecommendDTO> recommendList = OrderServ.recommendStock(itemName, user_seq);
		
		System.out.println("userSeq = " + user_seq);
	    System.out.println("itemName = " + itemName);
		
		return ResponseEntity.ok(recommendList);
	}
	
	// 거래처 품목 목록
	@GetMapping("/items")
	public ResponseEntity <List<OrderItemDTO>> compareItem(OrderItemDTO dto) {
		
		List<OrderItemDTO> list = OrderServ.compareItem(dto);
		
		return ResponseEntity.ok(list);
	}
	
	// 사업자명과 주소
	@GetMapping("/identity")
	public ResponseEntity<Map<String, Object>> identityCheck(HttpServletRequest request) {
		
		Long user_seq = (Long) request.getAttribute("user_seq");
		
		Map<String, Object> identity = OrderServ.identityCheck(user_seq);
		
		return ResponseEntity.ok(identity);
	}
	
	// 발주서 작성
	@PostMapping("/form")
	public ResponseEntity<Integer> orderForm(@RequestBody OrderDTO dto, HttpServletRequest request) {
		
		Long buyerSeq = (Long) request.getAttribute("user_seq");
		
		// 발주자는 프론트에서 받는 게 아니라 로그인한 사용자로 고정
		dto.setBuyerSeq(buyerSeq);
		
		// Service로 발주 저장 요청
	    // Service 안에서 orders 먼저 저장하고, order_items를 품목 개수만큼 저장함
		int result = OrderServ.orderForm(dto);
		
		return ResponseEntity.ok(result);
	}
	
	// 발주서 목록으로 출력 + 검색 및 필터링 기능
	@GetMapping("/list")
	public ResponseEntity<List<OrderListDTO>> orderList(
			HttpServletRequest request, 
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		
		Long userSeq = (Long) request.getAttribute("user_seq");
		
		List<OrderListDTO> orderList = OrderServ.orderList(userSeq, keyword, status, startDate, endDate);
		
		return ResponseEntity.ok(orderList);
	}
	
	// 웹소켓 사용자 확인
	@GetMapping("/me")
	public ResponseEntity<Long> webSocketMe(HttpServletRequest request) {
		
		Long webSocketMe = (Long) request.getAttribute("user_seq");
		
		return ResponseEntity.ok(webSocketMe);
	}
	
	// 웹소켓 테스트 API
	// 거래처가 상태 변경 버튼을 누른 것처럼 테스트하는 API
	// 예: POST /order/test/status/1?status=ACCEPTED
	@PostMapping("/test/status/{orderSeq}")
	public ResponseEntity<String> testUpdateOrderStatus(@PathVariable Long orderSeq, @RequestParam String status) {
	    OrderServ.updateOrderStatus(orderSeq, status);

	    return ResponseEntity.ok("발주 상태 변경 및 웹소켓 알림 전송 완료");
	}

}
