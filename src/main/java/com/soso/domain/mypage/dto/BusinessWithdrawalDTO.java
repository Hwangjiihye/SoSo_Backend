package com.soso.domain.mypage.dto;

public class BusinessWithdrawalDTO {
    private Long userSeq;
    private String withdrawReason;

    public BusinessWithdrawalDTO() {}

    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public String getWithdrawReason() {
        return withdrawReason;
    }

    public void setWithdrawReason(String withdrawReason) {
        this.withdrawReason = withdrawReason;
    }
}
