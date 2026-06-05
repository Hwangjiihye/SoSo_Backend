package com.soso.domain.order.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;

@Service
public class OrderService {
	
	@Autowired OrderDAO dao;
	
	// 사업자 재고 비교
	public List<OrderRecommendDTO> recommendStock(String itemName, Long userSeq) {
		
		return dao.recommendStock(itemName, userSeq);
	}
	
	// 거래처 품목 목록
	public List<OrderItemDTO> compareItem(OrderItemDTO dto) {
		
		return dao.compareItem(dto);
	}

}
