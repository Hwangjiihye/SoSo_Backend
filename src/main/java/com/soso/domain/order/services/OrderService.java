package com.soso.domain.order.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
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
	
	// 발주서 목록으로 출력 + 검색 및 필터링 기능
	public List<OrderListDTO> orderList(Long userSeq, Integer storeSeq, String keyword, String status, String startDate, String endDate) {
		return dao.orderList(userSeq, storeSeq, keyword, status, startDate, endDate);
	}

	// 발주서 상세 내역 조회
	public Map<String, Object> getOrderDetail(Long orderSeq) {
		Map<String, Object> result = new HashMap<>();
		
		// 1. 발주서 기본 정보 (상대 거래처명 포함)
		Map<String, Object> orderInfo = dao.findOrderInfoBySeq(orderSeq);
		result.put("orderInfo", orderInfo);
		
		// 2. 발주 품목 리스트
		List<Map<String, Object>> items = dao.findOrderItemsBySeq(orderSeq);
		result.put("items", items);
		
		return result;
	}
	
	// 웹소켓
	// 발주 상태 변경 + 사업자 화면에 웹소켓 알림 전송
	public void updateOrderStatus(Long orderSeq, String status) {

	    // 1. orders 테이블 status 변경
	    dao.updateOrderStatus(orderSeq, status);

	    // 2. 이 발주를 신청한 사업자 user_seq 조회
	    Long buyerSeq = dao.findBuyerSeqByOrderSeq(orderSeq);

	    // 3. 프론트로 보낼 데이터
	    Map<String, Object> message = new HashMap<>();
	    message.put("orderSeq", orderSeq);
	    message.put("status", status);
	    message.put("message", getStatusMessage(status));

	    // 4. 사업자 화면이 구독 중인 주소로 전송
	    System.out.println("웹소켓 전송 주소 = /sub/order/" + buyerSeq);
	    System.out.println("전송 message = " + message);
	    
	    messagingTemplate.convertAndSend("/sub/order/" + buyerSeq, (Object) message);
	}

	// 상태별 메시지
	private String getStatusMessage(String status) {
	    switch (status) {
	        case "ACCEPTED":
	            return "발주가 접수완료되었습니다.";
	        case "PREPARING":
	            return "상품준비가 시작되었습니다.";
	        case "SHIPPING":
	            return "상품이 배송중입니다.";
	        case "DELIVERED":
	            return "배송이 완료되었습니다.";
	        default:
	            return "발주 상태가 변경되었습니다.";
	    }
	}
}
