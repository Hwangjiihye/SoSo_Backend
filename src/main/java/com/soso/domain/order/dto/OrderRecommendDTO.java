package com.soso.domain.order.dto;

public class OrderRecommendDTO {
	
	private int stock_seq;
	private int user_seq;
	private String stock;
	private int quantity;
	private int safety_stock;
	
	public OrderRecommendDTO() {}
	public OrderRecommendDTO(int stock_seq, int user_seq, String stock, int quantity, int safety_stock) {
		super();
		this.stock_seq = stock_seq;
		this.user_seq = user_seq;
		this.stock = stock;
		this.quantity = quantity;
		this.safety_stock = safety_stock;
	}
	
	public int getStock_seq() {
		return stock_seq;
	}
	public void setStock_seq(int stock_seq) {
		this.stock_seq = stock_seq;
	}
	public int getUser_seq() {
		return user_seq;
	}
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
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
	public int getSafety_stock() {
		return safety_stock;
	}
	public void setSafety_stock(int safety_stock) {
		this.safety_stock = safety_stock;
	}
}
