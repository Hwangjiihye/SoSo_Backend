package com.soso.domain.order.dto;

/**
 * [거래처용 발주 상세 내역 DTO]
 * 특정 발주서 하나를 클릭했을 때, 그 안에 포함된 품목 리스트를 보여주기 위한 클래스입니다.
 * 초보자 가이드: '누가 주문했나'는 목록에서 보고, '뭘 주문했나'는 여기서 상세히 담습니다!
 * (롬복을 사용하지 않고 직접 생성자와 Getter/Setter를 작성한 버전입니다.)
 */
public class PartnerOrderDetailDTO {
    // 상세 품목의 고유 번호
    private Long orderItemSeq;
    
    // 품목 이름 (예: 서울우유 1L)
    private String itemName;
    
    // 카테고리 (예: 유제품)
    private String categoryName;
    
    // 주문 수량
    private Integer quantity;
    
    // 규격 (예: 10개입, 1박스 등)
    private String spec;
    
    // 개당 단가
    private Integer unitPrice;
    
    // 수량 * 단가 = 해당 품목의 총액
    private Integer totalPrice;

    // 1. 기본 생성자
    public PartnerOrderDetailDTO() {}

    // 2. 모든 필드를 포함하는 생성자
    public PartnerOrderDetailDTO(Long orderItemSeq, String itemName, String categoryName, Integer quantity, String spec, Integer unitPrice, Integer totalPrice) {
        this.orderItemSeq = orderItemSeq;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.spec = spec;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    // 3. Getter와 Setter
    public Long getOrderItemSeq() {
        return orderItemSeq;
    }

    public void setOrderItemSeq(Long orderItemSeq) {
        this.orderItemSeq = orderItemSeq;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    // toString
    @Override
    public String toString() {
        return "PartnerOrderDetailDTO [orderItemSeq=" + orderItemSeq + ", itemName=" + itemName + ", categoryName="
                + categoryName + ", quantity=" + quantity + ", spec=" + spec + ", unitPrice=" + unitPrice
                + ", totalPrice=" + totalPrice + "]";
    }
}
