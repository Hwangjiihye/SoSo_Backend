package com.soso.domain.order.dto;

public class OrderRecommendDTO {
	
	private int stockSeq;
	private int userSeq;
	private String stock;
	private int quantity;
	private int safetyStock;
	
	public OrderRecommendDTO() {}
	public OrderRecommendDTO(int stockSeq, int userSeq, String stock, int quantity, int safetyStock) {
		super();
		this.stockSeq = stockSeq;
		this.userSeq = userSeq;
		this.stock = stock;
		this.quantity = quantity;
		this.safetyStock = safetyStock;
	}
	
	public int getStockSeq() {
		return stockSeq;
	}
	public void setStockSeq(int stockSeq) {
		this.stockSeq = stockSeq;
	}
	public int getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(int userSeq) {
		this.userSeq = userSeq;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getSafetyStock() {
		return safetyStock;
	}
	public void setSafetyStock(int safetyStock) {
		this.safetyStock = safetyStock;
	}	
}
