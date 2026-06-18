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
//	public List<OrderRecommendDTO> recommendStock(String itemName, Object user_seq) {
//
//		Map<String, Object> params = new HashMap<>();
//		params.put("itemName", itemName);
//		params.put("user_seq", user_seq);
//
//		return mybatis.selectList("order.stockCheck", params);
//	}

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

	// 발주서 목록으로 출력 + 검색 기능
	public List<OrderListDTO> orderList(Long storeSeq, String keyword) {

	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);
	    params.put("keyword", keyword);

	    return mybatis.selectList("order.orderList", params);
	}
//	public List<OrderListDTO> orderList(Long userSeq, String keyword) {
//
//		Map<String, Object> params = new HashMap<>();
//		params.put("userSeq", userSeq);
//		params.put("keyword", keyword);
//
//		return mybatis.selectList("order.orderList", params);
//	}

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

}
