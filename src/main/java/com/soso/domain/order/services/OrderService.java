package com.soso.domain.order.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.OrderRecommendDTO;

@Service
public class OrderService {
	
	@Autowired OrderDAO dao;
	
	public List<OrderRecommendDTO> recommendStock(String itemName, String loginId) {
		
		return dao.recommendStock(itemName, loginId);
	}

}
