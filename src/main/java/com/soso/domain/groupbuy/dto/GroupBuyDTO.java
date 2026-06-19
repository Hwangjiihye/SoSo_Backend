package com.soso.domain.groupbuy.dto;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class GroupBuyDTO {
    private Integer groupBuySeq;
    private Integer userSeq;
    private Integer partnerSeq; // 추가: 거래처
    private Integer itemSeq;    // 품목명
    private String category;    // 추가: 카테고리
    private Integer quantity;   // 수량
    private Integer targetParticipants; // 모집인원
    private Integer currentParticipants;
    private Integer unitPrice;  // 추가: 단가
    private Integer totalAmount;// 총 결제금액
    private LocalDateTime endDate; // 마감기한
    private String pickupLocation; // 추가(변경): 픽업장소
    private String pickupTime;     // 추가(변경): 픽업가능시간
    private String notice;         // 추가(변경): 유의사항
    private String status;
    private LocalDateTime createdAt;
    
    // (선택) 기존 필드 유지
    private String groupName;
    private String description;

    public GroupBuyDTO() {}

    public Integer getGroupBuySeq() { return groupBuySeq; }
    public void setGroupBuySeq(Integer groupBuySeq) { this.groupBuySeq = groupBuySeq; }

    public Integer getUserSeq() { return userSeq; }
    public void setUserSeq(Integer userSeq) { this.userSeq = userSeq; }

    public Integer getPartnerSeq() { return partnerSeq; }
    public void setPartnerSeq(Integer partnerSeq) { this.partnerSeq = partnerSeq; }

    public Integer getItemSeq() { return itemSeq; }
    public void setItemSeq(Integer itemSeq) { this.itemSeq = itemSeq; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getTargetParticipants() { return targetParticipants; }
    public void setTargetParticipants(Integer targetParticipants) { this.targetParticipants = targetParticipants; }

    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Integer unitPrice) { this.unitPrice = unitPrice; }

    public Integer getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Integer totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getNotice() { return notice; }
    public void setNotice(String notice) { this.notice = notice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
