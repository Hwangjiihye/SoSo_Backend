package com.soso.domain.order.dto;

public class OrderListDTO {
	
	private Long orderSeq;
	private String orderNo;
	private Long sellerSeq;
	private Integer buyerStoreSeq;
	private String companyName;
	private String itemName;
	private String itemSummary;
	private String status;
	private String createdAt;
	private Integer totalAmount;
	
	public OrderListDTO() {}
	
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
	public Long getSellerSeq() {
		return sellerSeq;
	}
	public void setSellerSeq(Long sellerSeq) {
		this.sellerSeq = sellerSeq;
	}
	public Integer getBuyerStoreSeq() {
		return buyerStoreSeq;
	}
	public void setBuyerStoreSeq(Integer buyerStoreSeq) {
		this.buyerStoreSeq = buyerStoreSeq;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemSummary() {
		return itemSummary;
	}
	public void setItemSummary(String itemSummary) {
		this.itemSummary = itemSummary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
}
