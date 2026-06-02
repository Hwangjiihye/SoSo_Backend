package com.soso.domain.member.dto;

import java.time.LocalDate;

/**
 * 프론트엔드 리액트의 수평 JSON 구조를 그대로 수용하는 통합 DTO
 * 롬복(Lombok)을 사용하지 않는 순수 자바(Pure Java) 스타일
 */
public class SignUpDto {
    // 1. users 테이블 매핑 필드 (계정 정보)
    private Integer userSeq;    // PK (MyBatis useGeneratedKeys 연동)
    private String userId;      // user_id
    private String password;    // 비밀번호 (암호화 대상)
    private String name;        // 이름
    private String nickname;    // 닉네임
    private String email;       // 이메일
    private String phone;       // 전화번호
    private String userType;    // BUSINESS, PARTNER, ADMIN

    // 2. stores 테이블 매핑 필드 (매장 상세 정보)
    private Integer storeSeq;   // store_seq (PK)
    private String bizNo;       // biz_number
    private String corpName;    // company_name
    private String openDate;    // opening_date (String 형태)
    private String repName;		// 대표자명
    

	private Integer zipCode;    // zonecode
    private String address;     // address1
    private String detailAddress; // address2
    private String storeImage;  // store_image (결합된 URL 문자열)
    
    // 비즈니스 로직 처리를 위한 가공 필드
    private LocalDate formattedOpenDate; // DB DATE 타입 변환용

    public SignUpDto() {}

    // Getter / Setter (순차적 수동 작성)
    public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}
    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getStoreSeq() {
        return storeSeq;
    }

    public void setStoreSeq(Integer storeSeq) {
        this.storeSeq = storeSeq;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public LocalDate getFormattedOpenDate() {
        return formattedOpenDate;
    }

    public void setFormattedOpenDate(LocalDate formattedOpenDate) {
        this.formattedOpenDate = formattedOpenDate;
    }

    @Override
    public String toString() {
        return "SignUpDto{" +
                "userSeq=" + userSeq +
                ", userId='" + userId + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", storeImage='" + storeImage + '\'' +
                '}';
    }
}
