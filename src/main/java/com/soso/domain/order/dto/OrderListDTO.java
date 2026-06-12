package com.soso.domain.order.dto;

public class OrderListDTO {
	
	private Long orderSeq;
	private String orderNo;
	private Long sellerSeq;
	private String companyName;
	private String itemName;
	private String itemSummary;
	
	public OrderListDTO() {}
	public OrderListDTO(Long orderSeq, String orderNo, Long sellerSeq, String companyName, String itemName,
			String itemSummary) {
		super();
		this.orderSeq = orderSeq;
		this.orderNo = orderNo;
		this.sellerSeq = sellerSeq;
		this.companyName = companyName;
		this.itemName = itemName;
		this.itemSummary = itemSummary;
	}
	
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
}
