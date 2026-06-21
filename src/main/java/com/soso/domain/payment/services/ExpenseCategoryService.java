package com.soso.domain.payment.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.payment.dao.ExpenseCategoryDAO;
import com.soso.domain.payment.dto.ExpensesDTO;

@Service
public class ExpenseCategoryService {
	
	@Autowired
	private ExpenseCategoryDAO dao;
	
	// 지출 비용 등록
	public int insertCategory(ExpensesDTO dto) {
		return dao.insertCategory(dto);
	}
	
	// 월별 지출 비용 출력
	public Long monthlyTotal(Integer storeSeq, String month) {
		return dao.monthlyTotal(storeSeq, month);
	}
	
	// 월별 카테고리별 지출 출력
	public List<Map<String, Object>> categoryCost(Integer storeSeq, String month) {
		return dao.categoryCost(storeSeq, month);
	}
	
	// 카테고리별 월 지출 세부내역 출력
	public List<ExpensesDTO> expenseDetails(int storeSeq, String month, int categorySeq) {
	    return dao.expenseDetails(storeSeq, month, categorySeq);
	}
	
	// 비용 카테고리 - 식자재비 - 일반 발주 목록 조회
	public List<Map<String, Object>> generalOrdersForExpense(int storeSeq, int partnerStoreSeq) {
	    return dao.generalOrdersForExpense(storeSeq, partnerStoreSeq);
	}

}
