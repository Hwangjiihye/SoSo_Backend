package com.soso.domain.order.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.OrderDTO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderListDTO;
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
		
		// ↓ DB가 자동으로 만들어준 orderSeq를 이용해서 발주번호 orderNo를 만들고, 다시 orders 테이블에 저장하는 코드
		
		// 3. 오늘 날짜 생성  
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));  

		// 4. 발주번호 생성  
		String orderNo = String.format("ORD-%s-%03d", today, orderSeq);  

		// 5. DTO에 발주번호 넣기  
		dto.setOrderNo(orderNo);  

		// 6. orders 테이블에 order_no 업데이트  
		dao.updateOrderNo(dto);
		
		// 7. 발주 품목 목록 저장
	    for (OrderSaveItemDTO item : dto.getItems()) {
	        item.setOrderSeq(orderSeq);
	        dao.orderItem(item);
	    }

	    return result;
	}
	
	// 발주서 목록으로 출력 + 검색 기능
	public List<OrderListDTO> orderList(Long userSeq, String keyword) {
		return dao.orderList(userSeq, keyword);
	}
}
