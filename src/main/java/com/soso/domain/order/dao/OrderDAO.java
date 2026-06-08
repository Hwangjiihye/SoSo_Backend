package com.soso.domain.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.order.dto.OrderItemDTO;
import com.soso.domain.order.dto.OrderRecommendDTO;

@Repository
public class OrderDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;

	// 사업자 재고 비교
	public List<OrderRecommendDTO> recommendStock(String itemName, Object user_seq) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("itemName", itemName);
		params.put("user_seq", user_seq);
		
		return mybatis.selectList("order.stockCheck", params);
	}
	
	// 거래처 품목 목록
	public List<OrderItemDTO> compareItem(OrderItemDTO dto) {
		return mybatis.selectList("order.itemCheck", dto);
	}
	
	// 사업자명과 주소
	public Map<String, Object> identityCheck(Long user_seq) {
		return mybatis.selectOne("order.identityCheck", user_seq);
	}
}
