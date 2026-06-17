package com.soso.domain.product.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockHistoryDTO {
    private int historySeq;
    private int stockSeq;
    private int storeSeq;
    private Integer batchSeq;
    private String transactionType;
    private int changeQuantity;
    private int currentTotalStock;
    private String detailStockName;
    private int price;
    private LocalDate expirationDate;
    private String reason;
    private String memo;
    private LocalDateTime createdAt;

    public StockHistoryDTO() {}

    public int getHistorySeq() { return historySeq; }
    public void setHistorySeq(int historySeq) { this.historySeq = historySeq; }
    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public int getStoreSeq() { return storeSeq; }
    public void setStoreSeq(int storeSeq) { this.storeSeq = storeSeq; }
    public Integer getBatchSeq() { return batchSeq; }
    public void setBatchSeq(Integer batchSeq) { this.batchSeq = batchSeq; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public int getChangeQuantity() { return changeQuantity; }
    public void setChangeQuantity(int changeQuantity) { this.changeQuantity = changeQuantity; }
    public int getCurrentTotalStock() { return currentTotalStock; }
    public void setCurrentTotalStock(int currentTotalStock) { this.currentTotalStock = currentTotalStock; }
    public String getDetailStockName() { return detailStockName; }
    public void setDetailStockName(String detailStockName) { this.detailStockName = detailStockName; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
