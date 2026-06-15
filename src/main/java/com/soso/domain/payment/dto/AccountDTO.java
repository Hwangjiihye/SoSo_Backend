package com.soso.domain.payment.dto;

public class AccountDTO {
	
	private Long accountSeq;
	private Long storeSeq;
	private String bankName;
	private String accountNumber;
	private String accountName;
	private String billingKey;
	private String isActive;
	private String createAt;
	
	public AccountDTO() {}
	public AccountDTO(Long accountSeq, Long storeSeq, String bankName, String accountNumber, String accountName,
			String billingKey, String isActive, String createAt) {
		super();
		this.accountSeq = accountSeq;
		this.storeSeq = storeSeq;
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.billingKey = billingKey;
		this.isActive = isActive;
		this.createAt = createAt;
	}
	
	public Long getAccountSeq() {
		return accountSeq;
	}
	public void setAccountSeq(Long accountSeq) {
		this.accountSeq = accountSeq;
	}
	public Long getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(Long storeSeq) {
		this.storeSeq = storeSeq;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBillingKey() {
		return billingKey;
	}
	public void setBillingKey(String billingKey) {
		this.billingKey = billingKey;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
}
