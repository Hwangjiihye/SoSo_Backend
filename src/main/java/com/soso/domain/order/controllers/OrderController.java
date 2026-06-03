package com.soso.domain.order.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.order.dto.OrderRecommendDTO;
import com.soso.domain.order.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService OrderServ;
	
	@GetMapping("/check")
	public ResponseEntity <List<OrderRecommendDTO>> recommendStock(@RequestParam("itemName") String itemName, HttpServletRequest request) {
		
		String loginId = (String)request.getAttribute("loginId");
		
		List<OrderRecommendDTO> recommendList = OrderServ.recommendStock(itemName, loginId);
		
		System.out.println("loginId = " + loginId);
	    System.out.println("itemName = " + itemName);
		
		return ResponseEntity.ok(recommendList);
		
	}

}
