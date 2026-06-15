package com.soso.domain.payment.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soso.domain.payment.dao.AccountDAO;
import com.soso.domain.payment.dto.AccountDTO;

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

}