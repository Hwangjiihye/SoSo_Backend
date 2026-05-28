package com.soso.domain.member.dto;

import java.time.LocalDate;

public class SignUpDto {
    // 1. 계정 및 인적 사항 (프론트 formData와 Key 이름 1:1 매칭)
    private String userType;    // BUSINESS, PARTNER, ADMIN
    private String userId;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String name;
    private String phone;
    
    // 주민번호 앞 7자리
    private String ssnFront;
    private String ssnBack;
    
    private String email;

    // 2. 사업자 정보 
    private String bizNo;       
    private String repName;     
    private String openDate;    // 프론트에서 넘어오는 String 날짜
    private String corpName;    
    
    // DB 매핑용 (Service에서 가공하여 적재)
    private String storeImage;  // '외부경로;내부경로' 형태
    private LocalDate formattedOpenDate; // DB DATE 타입 매핑용

    // 3. 주소 정보
    private Integer zipCode;    
    private String address;      
    private String detailAddress; 

    // 기본 생성자
    public SignUpDto() {}

    // Getter / Setter (수동 생성)
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSsnFront() { return ssnFront; }
    public void setSsnFront(String ssnFront) { this.ssnFront = ssnFront; }

    public String getSsnBack() { return ssnBack; }
    public void setSsnBack(String ssnBack) { this.ssnBack = ssnBack; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBizNo() { return bizNo; }
    public void setBizNo(String bizNo) { this.bizNo = bizNo; }

    public String getRepName() { return repName; }
    public void setRepName(String repName) { this.repName = repName; }

    public String getOpenDate() { return openDate; }
    public void setOpenDate(String openDate) { this.openDate = openDate; }

    public String getCorpName() { return corpName; }
    public void setCorpName(String corpName) { this.corpName = corpName; }

    public String getStoreImage() { return storeImage; }
    public void setStoreImage(String storeImage) { this.storeImage = storeImage; }

    public LocalDate getFormattedOpenDate() { return formattedOpenDate; }
    public void setFormattedOpenDate(LocalDate formattedOpenDate) { this.formattedOpenDate = formattedOpenDate; }

    public Integer getZipCode() { return zipCode; }
    public void setZipCode(Integer zipCode) { this.zipCode = zipCode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDetailAddress() { return detailAddress; }
    public void setDetailAddress(String detailAddress) { this.detailAddress = detailAddress; }

    @Override
    public String toString() {
        return "SignUpDto{" +
                "userType='" + userType + '\'' +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", corpName='" + corpName + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", openDate='" + openDate + '\'' +
                '}';
    }
}
