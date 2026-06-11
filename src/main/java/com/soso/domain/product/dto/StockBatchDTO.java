package com.soso.domain.product.dto;

import java.time.LocalDate;

public class StockBatchDTO {
    private int batchSeq;
    private int stockSeq;
    private String lotNumber;
    private String detailStockName;
    private int initialQuantity;
    private int currentQuantity;
    private int incomingPrice;
    private LocalDate expirationDate;
    private LocalDate incomingDate;

    public StockBatchDTO() {}

    public int getBatchSeq() { return batchSeq; }
    public void setBatchSeq(int batchSeq) { this.batchSeq = batchSeq; }
    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getDetailStockName() { return detailStockName; }
    public void setDetailStockName(String detailStockName) { this.detailStockName = detailStockName; }
    public int getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(int initialQuantity) { this.initialQuantity = initialQuantity; }
    public int getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(int currentQuantity) { this.currentQuantity = currentQuantity; }
    public int getIncomingPrice() { return incomingPrice; }
    public void setIncomingPrice(int incomingPrice) { this.incomingPrice = incomingPrice; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public LocalDate getIncomingDate() { return incomingDate; }
    public void setIncomingDate(LocalDate incomingDate) { this.incomingDate = incomingDate; }
}
