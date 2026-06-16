package com.soso.domain.payment.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.payment.dto.PaymentDTO;
import com.soso.domain.payment.dto.AutoPaymentScheduleDTO;

@Repository
public class PaymentDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	// 계좌 등록
	public int insertAccount(PaymentDTO dto) {
		return mybatis.insert("payment.account", dto);
	}
	
	// 계좌 출력
	public List<PaymentDTO> accountList(Long storeSeq) {
		return mybatis.selectList("payment.list", storeSeq);
	}
	
	// 계좌 등록 4개 제한
	public int accountCount(Long storeSeq) {
		return mybatis.selectOne("payment.limit", storeSeq);
	}
	
	// 계좌 삭제 처리: 실제 삭제 X -> Update
	public int accountDel(Long accountSeq) {
		return mybatis.update("payment.accountDel", accountSeq);
	}
	
	// 자동이체 설정 등록
	public int autoPaymentSchedule(AutoPaymentScheduleDTO dto) {
		return mybatis.insert("payment.autoSchedule", dto);
	}
	

}
