package com.soso.domain.mypage.dto;

public class PartnerWithdrawalDTO {
	private int userSeq;          // 🎯 세션/토큰에서 뽑아낸 회원 고유 번호
    private String withdrawReason; // 🎯 리액트에서 사장님이 입력한 생생한 탈퇴 사유

    // 기본 생성자 (Jackson ObjectMapper가 역직렬화할 때 필수라네!)
    public PartnerWithdrawalDTO() {
    }

    // 편의를 위한 생성자
    public PartnerWithdrawalDTO(int userSeq, String withdrawReason) {
        this.userSeq = userSeq;
        this.withdrawReason = withdrawReason;
    }

    // 💡 Getter / Setter 장부
    public int getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }

    public String getWithdrawReason() {
        return withdrawReason;
    }

    public void setWithdrawReason(String withdrawReason) {
        this.withdrawReason = withdrawReason;
    }

    // 디버깅 로그 찍기 편하게 toString도 웅장하게 추가해두세!
    @Override
    public String toString() {
        return "WithdrawalDTO{" +
                "userSeq=" + userSeq +
                ", withdrawReason='" + withdrawReason + '\'' +
                '}';
    }
}
