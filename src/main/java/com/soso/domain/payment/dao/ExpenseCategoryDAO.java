package com.soso.domain.payment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.payment.dto.ExpensesDTO;

@Repository
public class ExpenseCategoryDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	// 지출 비용 등록
	public int insertCategory(ExpensesDTO dto) {
		return mybatis.insert("expense.insert", dto);
	}
	
	// 월별 지출 비용 출력
	public Long monthlyTotal(Integer storeSeq, String month) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("storeSeq", storeSeq);
		params.put("month", month);
		
		return mybatis.selectOne("expense.monthlyTotal", params);
	}
	
	// 월별 카테고리별 지출 비용 출력
	public List<Map<String, Object>> categoryCost(Integer storeSeq, String month) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("storeSeq", storeSeq);
		params.put("month", month);
		
		return mybatis.selectList("expense.monthlyTotalByCategory", params);
	}
	
	// 카테고리별 월 지출 세부내역 출력
	public List<ExpensesDTO> expenseDetails(int storeSeq, String month, int categorySeq) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);
	    params.put("month", month);
	    params.put("categorySeq", categorySeq);

	    return mybatis.selectList("expense.expenseDetails", params);
	}

}
