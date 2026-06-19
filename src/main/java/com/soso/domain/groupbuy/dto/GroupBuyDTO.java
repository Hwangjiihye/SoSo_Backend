package com.soso.domain.groupbuy.dto;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class GroupBuyDTO {
    private Integer groupBuySeq;
    private Integer userSeq;
    private Integer itemSeq;
    private String groupName;
    private String description;
    private Integer targetParticipants;
    private Integer currentParticipants;
    private Integer quantity;
    private Integer totalAmount;
    private LocalDateTime endDate;
    private String deliveryMethod;
    private LocalDate deliveryDate;
    private String deliveryNotice;
    private String status;
    private LocalDateTime createdAt;

    public GroupBuyDTO() {}

    public Integer getGroupBuySeq() {
        return groupBuySeq;
    }

    public void setGroupBuySeq(Integer groupBuySeq) {
        this.groupBuySeq = groupBuySeq;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public Integer getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(Integer itemSeq) {
        this.itemSeq = itemSeq;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTargetParticipants() {
        return targetParticipants;
    }

    public void setTargetParticipants(Integer targetParticipants) {
        this.targetParticipants = targetParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryNotice() {
        return deliveryNotice;
    }

    public void setDeliveryNotice(String deliveryNotice) {
        this.deliveryNotice = deliveryNotice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
