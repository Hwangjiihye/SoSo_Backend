package com.soso.domain.account.dto;

/**
 * @file AccountRelationRequestDto.java
 * @description 거래처 등록 요청 데이터를 담는 DTO입니다.
 */
public class AccountRelationRequestDto {
    private int businessSeq;
    private int partnerSeq;
    private String memo;

    public AccountRelationRequestDto() {
    }

    public AccountRelationRequestDto(int businessSeq, int partnerSeq, String memo) {
        this.businessSeq = businessSeq;
        this.partnerSeq = partnerSeq;
        this.memo = memo;
    }

    public int getBusinessSeq() {
        return businessSeq;
    }

    public void setBusinessSeq(int businessSeq) {
        this.businessSeq = businessSeq;
    }

    public int getPartnerSeq() {
        return partnerSeq;
    }

    public void setPartnerSeq(int partnerSeq) {
        this.partnerSeq = partnerSeq;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "AccountRelationRequestDto{" +
                "businessSeq=" + businessSeq +
                ", partnerSeq=" + partnerSeq +
                ", memo='" + memo + '\'' +
                '}';
    }
}
