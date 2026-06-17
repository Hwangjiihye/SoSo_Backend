package com.soso.domain.finance.dao;

import com.soso.domain.finance.dto.FinanceDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FinanceDAO {

    @Autowired
    private SqlSession sql;

    public List<FinanceDTO> getFinanceList(int userSeq, Integer storeSeq, String startDate, String endDate, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("userSeq", userSeq);
        params.put("storeSeq", storeSeq);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("type", type);
        return sql.selectList("FinanceMapper.getFinanceList", params);
    }

    public List<Map<String, Object>> getDailySummary(int userSeq, Integer storeSeq, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        params.put("userSeq", userSeq);
        params.put("storeSeq", storeSeq);
        params.put("yearMonth", yearMonth);
        return sql.selectList("FinanceMapper.getDailySummary", params);
    }
}
