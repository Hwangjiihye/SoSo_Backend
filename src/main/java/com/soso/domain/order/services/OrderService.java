package com.soso.domain.order.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.OrderDTO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;
import com.soso.domain.order.dto.OrderSaveItemDTO;

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
	
	// 발주서 작성
	public int orderForm(OrderDTO dto) {
		
		// 1. orders테이블에 발주 기본 정보 저장
		int result = dao.orderForm(dto);
		
		// 2. 방금 생성된 order_seq 가져오기
		Long orderSeq = dto.getOrderSeq();
		
		// 3. 발주 품목 목록 저장
	    for (OrderSaveItemDTO item : dto.getItems()) {
	        item.setOrderSeq(orderSeq);
	        dao.orderItem(item);
	    }

	    return result;
	}
}
