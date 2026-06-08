package com.soso.domain.order.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.order.dto.OrderItemDTO;
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

}
