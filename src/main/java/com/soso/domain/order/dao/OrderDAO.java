package com.soso.domain.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.order.dto.OrderRecommendDTO;

@Repository
public class OrderDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;

	public List<OrderRecommendDTO> recommendStock(String itemName, String loginId) {
		
		Map<String, String> params = new HashMap<>();
		params.put("itemName", itemName);
		params.put("loginId", loginId);
		
		return mybatis.selectList("order.check", params);
	}
}
