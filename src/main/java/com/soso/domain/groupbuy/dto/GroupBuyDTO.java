package com.soso.domain.groupbuy.dto;

import java.sql.Timestamp;

public class GroupBuyDTO {
    private int groupBuySeq;
    private int itemSeq;
    private Integer storeSeq;
    private String itemName;
    private int targetQuantity;
    private int currentQuantity;
    private Timestamp endDate;
    private String status;
    private Timestamp createdAt;

    public GroupBuyDTO() {}

    public int getGroupBuySeq() { return groupBuySeq; }
    public void setGroupBuySeq(int groupBuySeq) { this.groupBuySeq = groupBuySeq; }
    public int getItemSeq() { return itemSeq; }
    public void setItemSeq(int itemSeq) { this.itemSeq = itemSeq; }
    public Integer getStoreSeq() { return storeSeq; }
    public void setStoreSeq(Integer storeSeq) { this.storeSeq = storeSeq; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getTargetQuantity() { return targetQuantity; }
    public void setTargetQuantity(int targetQuantity) { this.targetQuantity = targetQuantity; }
    public int getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(int currentQuantity) { this.currentQuantity = currentQuantity; }
    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
