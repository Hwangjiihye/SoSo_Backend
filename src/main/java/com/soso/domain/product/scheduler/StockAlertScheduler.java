package com.soso.domain.product.scheduler;

import com.soso.domain.product.dao.StockDAO;
import com.soso.domain.product.dao.StockHistoryDAO;
import com.soso.domain.product.dto.StockBatchDTO;
import com.soso.domain.product.dto.StockDTO;
import com.soso.domain.product.dto.StockHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class StockAlertScheduler {

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private StockHistoryDAO stockHistoryDAO;

    /**
     * 매일 오전 9시에 유통기한 임박 품목을 체크하여 ALERT 이력을 생성합니다.
     * 기준: 유통기한이 7일 이내로 남은 배치
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void checkExpiringStock() {
        // 1. 유통기한 임박 배치 조회
        List<StockBatchDTO> expiringBatches = stockDAO.selectExpiringBatches();

        for (StockBatchDTO batch : expiringBatches) {
            // 2. 현재 해당 품목의 마스터 정보 조회 (현재 총 재고 확인용)
            StockDTO master = stockDAO.selectStockBySeq(batch.getStockSeq());
            
            if (master != null) {
                // 3. ALERT 이력 생성
                StockHistoryDTO alertHistory = new StockHistoryDTO();
                alertHistory.setStockSeq(batch.getStockSeq());
                alertHistory.setBatchSeq(batch.getBatchSeq());
                alertHistory.setTransactionType("ALERT");
                alertHistory.setChangeQuantity(0);
                alertHistory.setCurrentTotalStock(master.getCurrentStock());
                alertHistory.setDetailStockName(batch.getDetailStockName());
                alertHistory.setPrice(batch.getIncomingPrice());
                alertHistory.setExpirationDate(batch.getExpirationDate());
                alertHistory.setReason("유통기한 임박");
                alertHistory.setMemo("만료 예정일: " + batch.getExpirationDate() + " 확인 필요");
                
                stockHistoryDAO.insertStockHistory(alertHistory);
            }
        }
    }
}
