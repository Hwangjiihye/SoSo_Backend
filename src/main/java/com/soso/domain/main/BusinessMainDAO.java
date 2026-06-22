package com.soso.domain.main;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessMainDAO {

    @Autowired
    private SqlSession session;

    private static final String NAMESPACE = "businessMain.";

    public int selectTotalStocksCount(int storeSeq) {
        return session.selectOne(NAMESPACE + "selectTotalStocksCount", storeSeq);
    }

    public int selectLackStocksCount(int storeSeq) {
        return session.selectOne(NAMESPACE + "selectLackStocksCount", storeSeq);
    }

    public int selectExpiringSoonCount(int storeSeq) {
        return session.selectOne(NAMESPACE + "selectExpiringSoonCount", storeSeq);
    }

    public int selectActiveGroupBuysCount() {
        return session.selectOne(NAMESPACE + "selectActiveGroupBuysCount");
    }

    public List<Map<String, Object>> selectCurrentStockStatus(int storeSeq) {
        return session.selectList(NAMESPACE + "selectCurrentStockStatus", storeSeq);
    }

    public List<Map<String, Object>> selectMonthlySales(long userSeq) {
        return session.selectList(NAMESPACE + "selectMonthlySales", userSeq);
    }

    public List<Map<String, Object>> selectGroupOrders(Map<String, Object> params) {
        return session.selectList(NAMESPACE + "selectGroupOrders", params);
    }
}
