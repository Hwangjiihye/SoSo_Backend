package com.soso.domain.product.services;

import com.soso.domain.product.dao.StockDAO;
import com.soso.domain.product.dao.StockHistoryDAO;
import com.soso.domain.product.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class StockService {

    @Autowired
    private StockDAO stockDAO;

    @Autowired
    private StockHistoryDAO stockHistoryDAO;

    public List<StockDTO> getStockList(Map<String, Object> filters) {
        return stockDAO.selectStockList(filters);
    }

    public void createStock(int storeSeq,StockDTO stock) {
        stockDAO.insertStock(storeSeq,stock);
    }

    @Transactional
    public void processIncoming(StockIncomingDTO incoming, int storeSeq) {
        StockDTO master = stockDAO.selectStockBySeq(incoming.getStockSeq(), storeSeq);
        if (master == null) {
            throw new RuntimeException("존재하지 않는 품목입니다.");
        }
        
        // 1. 유통기한 자동 계산
        LocalDate expDate = incoming.getExpirationDate();
        if (expDate == null) {
            expDate = LocalDate.now().plusDays(master.getDefaultExpiryDays());
        }

        // 2. 로트 번호 생성 (LOT-YYYYMMDD-[stockSeq]-[랜덤3자리])
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String lotNumber = String.format("LOT-%s-%d-%03d", today, master.getStockSeq(), new Random().nextInt(1000));

        // 3. 배치 등록
        StockBatchDTO batch = new StockBatchDTO();
        batch.setStockSeq(incoming.getStockSeq());
        batch.setStoreSeq(storeSeq);
        batch.setLotNumber(lotNumber);
        batch.setDetailStockName(incoming.getDetailStockName());
        batch.setInitialQuantity(incoming.getQuantity());
        batch.setCurrentQuantity(incoming.getQuantity());
        batch.setIncomingPrice(incoming.getIncomingPrice());
        batch.setExpirationDate(expDate);
        stockDAO.insertBatch(batch);

        // 4. 마스터 총재고 업데이트
        updateMasterStock(incoming.getStockSeq(), incoming.getQuantity(), storeSeq);

        // 5. 이력 기록
        StockDTO updatedMaster = stockDAO.selectStockBySeq(incoming.getStockSeq(), storeSeq);
        StockHistoryDTO history = new StockHistoryDTO();
        history.setStockSeq(incoming.getStockSeq());
        history.setStoreSeq(storeSeq);
        history.setBatchSeq(batch.getBatchSeq());
        history.setTransactionType("INCOMING");
        history.setChangeQuantity(incoming.getQuantity());
        history.setCurrentTotalStock(updatedMaster.getCurrentStock());
        history.setDetailStockName(incoming.getDetailStockName());
        history.setPrice(incoming.getIncomingPrice());
        history.setExpirationDate(expDate);
        history.setReason(incoming.getReason());
        history.setMemo(incoming.getMemo());
        stockDAO.insertHistory(history);
    }

    @Transactional
    public void processOutbound(StockOutboundRequest request, int storeSeq) {
        int remainingToOut = request.getQuantity();
        
        // 1. FIFO 기준 배치 조회 (유통기한 임박순)
        List<StockBatchDTO> batches = stockDAO.selectAvailableBatches(request.getStockSeq(), storeSeq);
        
        if (batches.isEmpty() || batches.stream().mapToInt(StockBatchDTO::getCurrentQuantity).sum() < remainingToOut) {
            throw new RuntimeException("재고가 부족하여 출고를 완료할 수 없습니다.");
        }

        for (StockBatchDTO batch : batches) {
            if (remainingToOut <= 0) break;

            int deductQty = Math.min(batch.getCurrentQuantity(), remainingToOut);
            
            // 2. 배치 수량 차감
            batch.setCurrentQuantity(batch.getCurrentQuantity() - deductQty);
            stockDAO.updateBatchQuantity(batch);
            
            remainingToOut -= deductQty;
            
            // 3. 마스터 총재고 차감
            updateMasterStock(request.getStockSeq(), -deductQty, storeSeq);
            
            // 4. 이력 기록
            StockDTO currentMaster = stockDAO.selectStockBySeq(request.getStockSeq(), storeSeq);
            StockHistoryDTO history = new StockHistoryDTO();
            history.setStockSeq(request.getStockSeq());
            history.setStoreSeq(storeSeq);
            history.setBatchSeq(batch.getBatchSeq());
            history.setTransactionType("OUTBOUND");
            history.setChangeQuantity(-deductQty);
            history.setCurrentTotalStock(currentMaster.getCurrentStock());
            history.setReason(request.getReason());
            history.setMemo(request.getMemo());
            history.setDetailStockName(batch.getDetailStockName()); 
            history.setExpirationDate(batch.getExpirationDate());
            history.setPrice(batch.getIncomingPrice());
            stockDAO.insertHistory(history);
        }

        // 5. 안전재고 미달 체크 (ALERT)
        StockDTO finalMaster = stockDAO.selectStockBySeq(request.getStockSeq(), storeSeq);
        if (finalMaster != null && finalMaster.getCurrentStock() < finalMaster.getSafetyStock()) {
            StockHistoryDTO alertHistory = new StockHistoryDTO();
            alertHistory.setStockSeq(request.getStockSeq());
            alertHistory.setStoreSeq(storeSeq);
            alertHistory.setTransactionType("ALERT");
            alertHistory.setChangeQuantity(0);
            alertHistory.setCurrentTotalStock(finalMaster.getCurrentStock());
            alertHistory.setReason("안전재고 미달");
            alertHistory.setDetailStockName(finalMaster.getStockName());
            alertHistory.setMemo("현재 " + finalMaster.getCurrentStock() + "개 - 안전 재고 미만");
            stockHistoryDAO.insertStockHistory(alertHistory);
        }
    }

    @Transactional
    public void processAdjust(StockAdjustRequest request, int storeSeq) {
        // 1. 특정 배치 조정 시
        if (request.getBatchSeq() != null && request.getBatchSeq() > 0) {
            List<StockBatchDTO> batches = stockDAO.selectBatchesByStockSeq(request.getStockSeq(), storeSeq);
            StockBatchDTO targetBatch = batches.stream()
                .filter(b -> b.getBatchSeq() == request.getBatchSeq())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 배치를 찾을 수 없습니다."));
            
            targetBatch.setCurrentQuantity(targetBatch.getCurrentQuantity() + request.getChangeQuantity());
            if (targetBatch.getCurrentQuantity() < 0) {
                throw new RuntimeException("조정 후 배치 재고가 0보다 작을 수 없습니다.");
            }
            stockDAO.updateBatchQuantity(targetBatch);
        }

        // 2. 마스터 업데이트
        updateMasterStock(request.getStockSeq(), request.getChangeQuantity(), storeSeq);
        
        // 3. 이력 기록
        StockDTO currentMaster = stockDAO.selectStockBySeq(request.getStockSeq(), storeSeq);
        if (currentMaster.getCurrentStock() < 0) {
             throw new RuntimeException("조정 후 총 재고가 0보다 작을 수 없습니다.");
        }
        
        StockHistoryDTO history = new StockHistoryDTO();
        history.setStockSeq(request.getStockSeq());
        history.setStoreSeq(storeSeq);
        history.setBatchSeq(request.getBatchSeq());
        history.setTransactionType("ADJUST");
        history.setChangeQuantity(request.getChangeQuantity());
        history.setCurrentTotalStock(currentMaster.getCurrentStock());
        history.setReason(request.getReason());
        history.setMemo(request.getMemo());
        stockDAO.insertHistory(history);
    }

    private void updateMasterStock(int stockSeq, int changeQty, int storeSeq) {
        Map<String, Object> params = new HashMap<>();
        params.put("stockSeq", stockSeq);
        params.put("storeSeq", storeSeq);
        params.put("changeQuantity", changeQty);
        stockDAO.updateCurrentStock(params);
    }

    public List<StockBatchDTO> getBatches(int stockSeq, int storeSeq) {
        return stockDAO.selectBatchesByStockSeq(stockSeq, storeSeq);
    }

    public List<StockHistoryDTO> getHistories(int stockSeq, int storeSeq) {
        return stockDAO.selectHistoriesByStockSeq(stockSeq, storeSeq);
    }

    public void updateStockInfo(StockDTO stock) {
        stockDAO.updateStock(stock);
    }

    @Transactional
    public void deleteStock(int stockSeq, int storeSeq) {
        // 1. 연관된 이력 삭제
        stockDAO.deleteHistoryByStockSeq(stockSeq, storeSeq);
        // 2. 연관된 배치 삭제
        stockDAO.deleteBatchesByStockSeq(stockSeq, storeSeq);
        // 3. 품목 마스터 삭제
        stockDAO.deleteStock(stockSeq, storeSeq);
    }

    public int getcountExpiringSoon(int storeSeq) {
        return stockDAO.selectgetcountExpiringSoon(storeSeq);
    }
}
