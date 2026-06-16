package com.soso.domain.product.dao;

import com.soso.domain.product.dto.StockHistoryDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StockHistoryDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.mappers.product.StockHistoryMapper.";

    /**
     * 재고 변동 이력 기록
     */
    public int insertStockHistory(StockHistoryDTO history) {
        return sqlSession.insert(NAMESPACE + "insertStockHistory", history);
    }

    /**
     * 대시보드 메인용 최신 5건 고정 조회
     */
    public List<StockHistoryDTO> getTop5StockHistory() {
        return sqlSession.selectList(NAMESPACE + "getTop5StockHistory");
    }

    /**
     * 팝업 모달창용 페이징 쿼리
     */
    public List<StockHistoryDTO> getStockHistoryWithPaging(int offset, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("size", size);
        return sqlSession.selectList(NAMESPACE + "getStockHistoryWithPaging", params);
    }

    /**
     * 전체 이력 개수 조회 (페이징용)
     */
    public int getTotalHistoryCount() {
        return sqlSession.selectOne(NAMESPACE + "getTotalHistoryCount");
    }
}
