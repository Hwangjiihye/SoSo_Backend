package com.soso.domain.payment.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.payment.dao.AccountDAO;
import com.soso.domain.payment.dto.AccountDTO;
import com.soso.domain.payment.dto.AutoPaymentScheduleDTO;

@Service
public class AccountService {
	
	@Autowired
	private AccountDAO dao;
	
	// 계좌 등록 + 4개 제한
	public int insertAccount(AccountDTO dto) {
		
		int limit = dao.accountCount(dto.getStoreSeq());
		
		if(limit >= 4) {
			throw new RuntimeException("계좌는 최대 4개까지 등록할 수 있습니다.");
		}
		
		String billingKey = "TEST_BILLING_KEY" + UUID.randomUUID();
		dto.setBillingKey(billingKey);
		
		System.out.println("생성된 billingKey = " + dto.getBillingKey());
		
		return dao.insertAccount(dto);
	}
	
	// 계좌 출력
	public List<AccountDTO> accountList(Long storeSeq) {
		
		return dao.accountList(storeSeq);
	}
	
	// 계좌 삭제 : 실제 삭제 X -> Update
	public int accountDel(Long accountSeq) {
		return dao.accountDel(accountSeq);
	}
	
	// 자동이체 설정 등록
	public int autoPaymentSchedule(AutoPaymentScheduleDTO dto) {
	    if (dto.getStoreSeq() == 0) {
	        throw new RuntimeException("사업장 정보가 없습니다.");
	    }

	    if (dto.getPartnerSeq() == 0) {
	        throw new RuntimeException("거래처를 선택해 주세요.");
	    }

	    if (dto.getAccountSeq() == 0) {
	        throw new RuntimeException("출금 계좌를 선택해 주세요.");
	    }

	    if (dto.getPaymentDay() == 0 || dto.getPaymentDay() < 1 || dto.getPaymentDay() > 31) {
	        throw new RuntimeException("결제일은 1일부터 31일 사이로 선택해 주세요.");
	    }

	    return dao.autoPaymentSchedule(dto);
	}

}