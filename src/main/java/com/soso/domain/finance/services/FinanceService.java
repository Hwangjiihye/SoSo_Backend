package com.soso.domain.finance.services;

import com.soso.domain.finance.dao.FinanceDAO;
import com.soso.domain.finance.dto.FinanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FinanceService {

    @Autowired
    private FinanceDAO financeDAO;

    public List<FinanceDTO> getFinanceList(int userSeq, Integer storeSeq, String startDate, String endDate, String type) {
        return financeDAO.getFinanceList(userSeq, storeSeq, startDate, endDate, type);
    }

    public List<Map<String, Object>> getDailySummary(int userSeq, Integer storeSeq, String yearMonth) {
        return financeDAO.getDailySummary(userSeq, storeSeq, yearMonth);
    }
}
