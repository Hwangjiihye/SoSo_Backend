package com.soso.domain.product.dto;

import java.time.LocalDateTime;

public class StockDTO {
    private int stockSeq;
    private int storeSeq;
    private String stockName;
    private String category;
    private String unit;
    private int safetyStock;
    private int defaultExpiryDays;
    private int currentStock;
    private int daysUntilExpiry; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockDTO() {}

    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public int getStoreSeq() { return storeSeq; }
    public void setStoreSeq(int storeSeq) { this.storeSeq = storeSeq; }
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public int getSafetyStock() { return safetyStock; }
    public void setSafetyStock(int safetyStock) { this.safetyStock = safetyStock; }
    public int getDefaultExpiryDays() { return defaultExpiryDays; }
    public void setDefaultExpiryDays(int defaultExpiryDays) { this.defaultExpiryDays = defaultExpiryDays; }
    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }
    public int getDaysUntilExpiry() { return daysUntilExpiry; }
    public void setDaysUntilExpiry(int daysUntilExpiry) { this.daysUntilExpiry = daysUntilExpiry; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
