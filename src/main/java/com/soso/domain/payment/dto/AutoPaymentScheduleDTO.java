package com.soso.domain.payment.dto;

public class AutoPaymentScheduleDTO {
	
	private int scheduleSeq;
	private int storeSeq;
	private int partnerSeq;
	private int accountSeq;
	private int paymentDay;
	private String isActive;
	private int startDate;
	private String createdAt;
	
	public AutoPaymentScheduleDTO() {}
	public AutoPaymentScheduleDTO(int scheduleSeq, int storeSeq, int partnerSeq, int accountSeq, int paymentDay,
			String isActive, int startDate, String createdAt) {
		super();
		this.scheduleSeq = scheduleSeq;
		this.storeSeq = storeSeq;
		this.partnerSeq = partnerSeq;
		this.accountSeq = accountSeq;
		this.paymentDay = paymentDay;
		this.isActive = isActive;
		this.startDate = startDate;
		this.createdAt = createdAt;
	}
	
	public int getScheduleSeq() {
		return scheduleSeq;
	}
	public void setScheduleSeq(int scheduleSeq) {
		this.scheduleSeq = scheduleSeq;
	}
	public int getStoreSeq() {
		return storeSeq;
	}
	public void setStoreSeq(int storeSeq) {
		this.storeSeq = storeSeq;
	}
	public int getPartnerSeq() {
		return partnerSeq;
	}
	public void setPartnerSeq(int partnerSeq) {
		this.partnerSeq = partnerSeq;
	}
	public int getAccountSeq() {
		return accountSeq;
	}
	public void setAccountSeq(int accountSeq) {
		this.accountSeq = accountSeq;
	}
	public int getPaymentDay() {
		return paymentDay;
	}
	public void setPaymentDay(int paymentDay) {
		this.paymentDay = paymentDay;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public int getStartDate() {
		return startDate;
	}
	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
