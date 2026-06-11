package com.soso.domain.product.dto;

import java.time.LocalDate;

public class StockIncomingDTO {
    private int stockSeq;
    private String detailStockName;
    private int quantity;
    private int incomingPrice;
    private LocalDate expirationDate;
    private String reason;
    private String memo;

    public StockIncomingDTO() {}

    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public String getDetailStockName() { return detailStockName; }
    public void setDetailStockName(String detailStockName) { this.detailStockName = detailStockName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getIncomingPrice() { return incomingPrice; }
    public void setIncomingPrice(int incomingPrice) { this.incomingPrice = incomingPrice; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
