package com.soso.domain.groupbuy.dto;

import java.time.LocalDateTime;

public class GroupBuyParticipantDTO {
    private Integer participantSeq;
    private Integer groupBuySeq;
    private Integer userSeq;
    private String paymentStatus;
    private String deliveryStatus;
    private LocalDateTime createdAt;

    public GroupBuyParticipantDTO() {}

    public Integer getParticipantSeq() {
        return participantSeq;
    }

    public void setParticipantSeq(Integer participantSeq) {
        this.participantSeq = participantSeq;
    }

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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
