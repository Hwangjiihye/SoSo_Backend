package com.soso.domain.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.order.dto.OrderDTO;
import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderListDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;
import com.soso.domain.order.dto.OrderSaveItemDTO;

@Repository
public class OrderDAO {

	@Autowired
	private SqlSessionTemplate mybatis;

	// 사업자 재고 비교
	public List<OrderRecommendDTO> recommendStock(String itemName, Long storeSeq) {

	    Map<String, Object> params = new HashMap<>();
	    params.put("itemName", itemName);
	    params.put("storeSeq", storeSeq);

	    return mybatis.selectList("order.stockCheck", params);
	}

	// 거래처 품목 목록
	public List<OrderItemDTO> compareItem(OrderItemDTO dto) {
		return mybatis.selectList("order.itemCheck", dto);
	}

	// 사업자명과 주소
	public Map<String, Object> identityCheck(Long storeSeq) {
		return mybatis.selectOne("order.identityCheck", storeSeq);
	}

	// 발주서 작성
	public int orderForm(OrderDTO dto) {
		return mybatis.insert("order.orderForm", dto);
	}
	
	// 발주 신청시 공급업체 목록 조회
	public List<OrderItemDTO> suppliers() {
	    return mybatis.selectList("order.suppliers");
	}

	// 발주서 품목 1줄
	public int orderItem(OrderSaveItemDTO dto) {
		return mybatis.insert("order.orderItem", dto);
	}

	// 발주번호(order_no) 업데이트
	public int updateOrderNo(OrderDTO dto) {
		return mybatis.update("order.updateOrderNo", dto);
	}

	
	// 발주서 목록으로 출력 + 검색 및 필터링 기능
	public List<OrderListDTO> orderList(
	        Long storeSeq,
	        Integer offset,
	        String keyword,
	        String status,
	        String startDate,
	        String endDate
	) {
	    Map<String, Object> params = new HashMap<>();

	    params.put("storeSeq", storeSeq);
	    params.put("offset", offset);
	    params.put("keyword", keyword);
	    params.put("status", status);
	    params.put("startDate", startDate);
	    params.put("endDate", endDate);

	    return mybatis.selectList("order.orderList", params);
	}
	

	// 웹소켓
	// 발주 상태 변경
	public int updateOrderStatus(Long orderSeq, String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderSeq", orderSeq);
		params.put("status", status);

		return mybatis.update("order.updateOrderStatus", params);
	}

	// orderSeq로 사업자 buyer_seq 조회
	public Long findBuyerSeqByOrderSeq(Long orderSeq) {
		return mybatis.selectOne("order.findBuyerSeqByOrderSeq", orderSeq);
	}
	
	// 발주 상세 - 기본 정보
	public Map<String, Object> findOrderInfoBySeq(Long orderSeq) {
		return mybatis.selectOne("order.findOrderInfoBySeq", orderSeq);
	}

	// 발주 상세 - 품목 리스트
	public List<Map<String, Object>> findOrderItemsBySeq(Long orderSeq) {
		return mybatis.selectList("order.findOrderItemsBySeq", orderSeq);
	}
	
	// 발주 미결제
	public List<Map<String, Object>> unpaidOrders(Long storeSeq, Long partnerSeq) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);
	    params.put("partnerSeq", partnerSeq);

	    return mybatis.selectList("order.unpaidOrders", params);
	}
	
	// RAG upsert용 발주 상세 조회
	// 발주 등록 직후 orderSeq로 발주 기본정보 + 거래처명 + 품목 요약을 가져오기 위해 사용
	// OrderService의 upsertOrderRag(orderSeq)에서 호출됨
	public Map<String, Object> selectOrderRag(Long orderSeq) {

	    // order.xml에 추가할 <select id="selectOrderRag"> SQL을 실행하는 코드
	    // mapper namespace가 "order"라서 "order.selectOrderRag"로 작성
	    return mybatis.selectOne("order.selectOrderRag", orderSeq);
	}
}
