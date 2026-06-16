package com.soso.domain.payment.dto;

public class PaymentCardDTO {
	
	private Long cardSeq;
	private Long userSeq;
	private Long storeSeq;
	private String billingKey;
	private String cardCompany;
	private String cardNumberMasked;
	private String cardType;
	private String cardName;
	private String isDefault;
	private String isActive;
	private String createdAt;
	
	public PaymentCardDTO() {}
	public PaymentCardDTO(Long cardSeq, Long userSeq, Long storeSeq, String billingKey, String cardCompany,
			String cardNumberMasked, String cardType, String cardName, String isDefault, String isActive,
			String createdAt) {
		super();
		this.cardSeq = cardSeq;
		this.userSeq = userSeq;
		this.storeSeq = storeSeq;
		this.billingKey = billingKey;
		this.cardCompany = cardCompany;
		this.cardNumberMasked = cardNumberMasked;
		this.cardType = cardType;
		this.cardName = cardName;
		this.isDefault = isDefault;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}
	
	public Long getCardSeq() {
		return cardSeq;
	}
	public void setCardSeq(Long cardSeq) {
		this.cardSeq = cardSeq;
	}
	public Long getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(Long userSeq) {
		this.userSeq = userSeq;
	}
	public Long getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(Long storeSeq) {
		this.storeSeq = storeSeq;
	}
	public String getBillingKey() {
		return billingKey;
	}
	public void setBillingKey(String billingKey) {
		this.billingKey = billingKey;
	}
	public String getCardCompany() {
		return cardCompany;
	}
	public void setCardCompany(String cardCompany) {
		this.cardCompany = cardCompany;
	}
	public String getCardNumberMasked() {
		return cardNumberMasked;
	}
	public void setCardNumberMasked(String cardNumberMasked) {
		this.cardNumberMasked = cardNumberMasked;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
