package com.soso.domain.product.dto;

public class StockOutboundRequest {
    private int stockSeq;
    private int quantity;
    private String reason;
    private String memo;

    public StockOutboundRequest() {}

    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
