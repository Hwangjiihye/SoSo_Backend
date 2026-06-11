package com.soso.domain.order.dto;

import java.util.Date;

/**
 * [거래처용 발주 목록 DTO]
 * 거래처(Partner) 사장님이 자신에게 들어온 전체 주문 리스트를 볼 때 사용하는 클래스입니다.
 * 초보자 가이드: DTO는 DB에서 가져온 데이터를 담아서 화면으로 전달하는 '바구니'라고 생각하면 됩니다!
 * (롬복을 사용하지 않고 직접 생성자와 Getter/Setter를 작성한 버전입니다.)
 */
public class PartnerOrderListDTO {
    // 발주서의 고유 식별 번호 (PK)
    private Long orderSeq;
    
    // 화면에 보여줄 발주 번호 (예: ORD-20240611-001)
    private String orderNo;
    
    // 주문을 한 업장(소상공인)의 상호명
    private String companyName;
    
    // 이번 발주의 전체 금액
    private Long totalAmount;
    
    // 현재 발주의 상태 (예: REQUESTED - 발주신청, ACCEPTED - 접수완료 등)
    private String status;
    
    // 발주가 생성된 날짜와 시간
    private Date createdAt;

    // 1. 기본 생성자 (파라미터가 없는 빈 바구니)
    public PartnerOrderListDTO() {}

    // 2. 모든 필드를 포함하는 생성자
    public PartnerOrderListDTO(Long orderSeq, String orderNo, String companyName, Long totalAmount, String status, Date createdAt) {
        this.orderSeq = orderSeq;
        this.orderNo = orderNo;
        this.companyName = companyName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // 3. Getter와 Setter (데이터를 넣고 빼는 구멍들)
    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // toString (객체 정보를 문자열로 확인하기 위한 용도)
    @Override
    public String toString() {
        return "PartnerOrderListDTO [orderSeq=" + orderSeq + ", orderNo=" + orderNo + ", companyName=" + companyName
                + ", totalAmount=" + totalAmount + ", status=" + status + ", createdAt=" + createdAt + "]";
    }
}
