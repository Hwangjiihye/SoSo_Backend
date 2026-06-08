package com.soso.domain.order.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;

@Service
public class OrderService {
	
	@Autowired OrderDAO dao;
	
	// 사업자 재고 비교
	public List<OrderRecommendDTO> recommendStock(String itemName, Long user_seq) {
		
		return dao.recommendStock(itemName, user_seq);
	}
	
	// 거래처 품목 목록
	public List<OrderItemDTO> compareItem(OrderItemDTO dto) {
		
		return dao.compareItem(dto);
	}
	
	// 사업자명과 주소
	public Map<String, Object> identityCheck(Long user_seq) {
		return dao.identityCheck(user_seq);
	}
}
