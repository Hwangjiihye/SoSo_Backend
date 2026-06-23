package com.soso.domain.order.dto;

public class OrderRecommendDTO { // 거래처 품목명과 내 재고를 비교해서 추천 결과 보여줄 때 사용
	
	private int stockSeq;
	private int storeSeq;
	private String stock;
	private int quantity;
	private int safetyStock;
	
	public OrderRecommendDTO() {}
	public OrderRecommendDTO(int stockSeq, int storeSeq, String stock, int quantity, int safetyStock) {
		super();
		this.stockSeq = stockSeq;
		this.storeSeq = storeSeq;
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
	public int getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(int storeSeq) {
		this.storeSeq = storeSeq;
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
