package com.soso.domain.mypage.dto;

import org.springframework.web.multipart.MultipartFile;

public class BusinessUpdateDTO {
    // User 필드
    private String nickname;
    private String phone;
    private String email;
    
    // Store 필드
    private String bizNumber;
    private String companyName;
    private Integer zonecode;
    private String address1;
    private String address2;

    // 식별용
    private Long userSeq;

    // 이미지 파일 (한번에 받기 위해 추가)
    private MultipartFile exteriorImg;
    private MultipartFile interiorImg;

    public BusinessUpdateDTO() {}

    // Getters and Setters
    public String getBizNumber() { return bizNumber; }
    public void setBizNumber(String bizNumber) { this.bizNumber = bizNumber; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getZonecode() { return zonecode; }
    public void setZonecode(Integer zonecode) { this.zonecode = zonecode; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public Long getUserSeq() { return userSeq; }
    public void setUserSeq(Long userSeq) { this.userSeq = userSeq; }

    public MultipartFile getExteriorImg() { return exteriorImg; }
    public void setExteriorImg(MultipartFile exteriorImg) { this.exteriorImg = exteriorImg; }

    public MultipartFile getInteriorImg() { return interiorImg; }
    public void setInteriorImg(MultipartFile interiorImg) { this.interiorImg = interiorImg; }
}
