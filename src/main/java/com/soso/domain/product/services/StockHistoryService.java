package com.soso.domain.product.services;

import com.soso.domain.product.dao.StockHistoryDAO;
import com.soso.domain.product.dto.StockHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockHistoryService {

    @Autowired
    private StockHistoryDAO stockHistoryDAO;

    /**
     * 대시보드 메인 화면용 최신 5건 조회
     */
    public List<StockHistoryDTO> getDashboardHistory() {
        return stockHistoryDAO.getTop5StockHistory();
    }

    /**
     * 모달창용 페이징 데이터 조회
     */
    public Map<String, Object> getModalHistory(int page, int size) {
        int offset = (page - 1) * size;
        
        List<StockHistoryDTO> historyList = stockHistoryDAO.getStockHistoryWithPaging(offset, size);
        int totalCount = stockHistoryDAO.getTotalHistoryCount();
        
        Map<String, Object> response = new HashMap<>();
        response.put("historyList", historyList);
        response.put("totalCount", totalCount);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));
        
        return response;
    }

    /**
     * 재고 변동 이력 기록 (필요 시 서비스 간 호출용)
     */
    @Transactional
    public void recordHistory(StockHistoryDTO history) {
        stockHistoryDAO.insertStockHistory(history);
    }
}
