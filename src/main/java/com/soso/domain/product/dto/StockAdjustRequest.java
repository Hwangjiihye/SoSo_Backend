package com.soso.domain.product.dto;

public class StockAdjustRequest {
    private int stockSeq;
    private Integer batchSeq;
    private int changeQuantity;
    private String reason;
    private String memo;

    public StockAdjustRequest() {}

    public int getStockSeq() { return stockSeq; }
    public void setStockSeq(int stockSeq) { this.stockSeq = stockSeq; }
    public Integer getBatchSeq() { return batchSeq; }
    public void setBatchSeq(Integer batchSeq) { this.batchSeq = batchSeq; }
    public int getChangeQuantity() { return changeQuantity; }
    public void setChangeQuantity(int changeQuantity) { this.changeQuantity = changeQuantity; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}
