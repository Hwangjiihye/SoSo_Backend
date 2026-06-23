package com.soso.domain.payment.dto;

public class ExpenseCategoryDTO {
	
	private int categorySeq;
	private String categoryName;
	private String color;
	private int sortOrder;
	private String createdAt;
	
	public ExpenseCategoryDTO() {}
	public ExpenseCategoryDTO(int categorySeq, String categoryName, String color, int sortOrder, String createdAt) {
		super();
		this.categorySeq = categorySeq;
		this.categoryName = categoryName;
		this.color = color;
		this.sortOrder = sortOrder;
		this.createdAt = createdAt;
	}
	
	public int getCategorySeq() {
		return categorySeq;
	}
	public void setCategorySeq(int categorySeq) {
		this.categorySeq = categorySeq;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
