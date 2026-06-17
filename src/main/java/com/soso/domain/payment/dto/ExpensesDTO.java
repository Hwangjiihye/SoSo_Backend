package com.soso.domain.payment.dto;

public class ExpensesDTO {
	
	private Integer expenseSeq;
	private Integer storeSeq;
	private Integer categorySeq;
	private String expenseDate;
	private String title;
	private Integer amount;
	private String memo;
	private String paymentMethod;
	private String supplierName;
	private String refType;
	private Integer refSeq;
	private String createdAt;
	
	public ExpensesDTO() {}
	public ExpensesDTO(Integer expenseSeq, Integer storeSeq, Integer categorySeq, String expenseDate, String title,
			Integer amount, String memo, String paymentMethod, String supplierName, String refType, Integer refSeq,
			String createdAt) {
		super();
		this.expenseSeq = expenseSeq;
		this.storeSeq = storeSeq;
		this.categorySeq = categorySeq;
		this.expenseDate = expenseDate;
		this.title = title;
		this.amount = amount;
		this.memo = memo;
		this.paymentMethod = paymentMethod;
		this.supplierName = supplierName;
		this.refType = refType;
		this.refSeq = refSeq;
		this.createdAt = createdAt;
	}
	
	public Integer getExpenseSeq() {
		return expenseSeq;
	}
	public void setExpenseSeq(Integer expenseSeq) {
		this.expenseSeq = expenseSeq;
	}
	public Integer getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(Integer storeSeq) {
		this.storeSeq = storeSeq;
	}
	public Integer getCategorySeq() {
		return categorySeq;
	}
	public void setCategorySeq(Integer categorySeq) {
		this.categorySeq = categorySeq;
	}
	public String getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public Integer getRefSeq() {
		return refSeq;
	}
	public void setRefSeq(Integer refSeq) {
		this.refSeq = refSeq;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
