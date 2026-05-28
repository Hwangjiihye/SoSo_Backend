package com.soso.domain.member.dto;

public class SignUpDto {
	// 1. 계정 및 인적 사항 (프론트 formData와 Key 이름 1:1 매칭)
    private String userId;
    private String password;
    private String confirmPassword; // 비밀번호 재확인 검증용
    private String nickname;
    private String name;
    private String phone;
    
    // 주민번호 앞 7자리 (DB에는 안 넣지만 프론트가 보내주므로 DTO에서 받아줌)
    private String ssnFront;
    private String ssnBack;
    
    private String email;

    // 2. 사업자 정보 
    private String bizNo;       // DB의 biz_number와 매칭될 녀석
    private String repName;     // 대표자명
    private String openDate;    // 프론트의 이 주소는 String으로 받아서 서비스에서 LocalDate로 파싱하는 게 안전하네
    private String corpName;    // DB의 company_name과 매칭될 녀석

    // 3. 주소 정보
    private Integer zipCode;    // DB의 zonecode와 매칭
    private String address;      // DB의 address1과 매칭
    private String detailAddress; // DB의 address2과 매칭
    
    
	public SignUpDto() {
		super();
	}
	public SignUpDto(String userId, String password, String confirmPassword, String nickname, String name, String phone,
			String ssnFront, String ssnBack, String email, String bizNo, String repName, String openDate,
			String corpName, Integer zipCode, String address, String detailAddress) {
		super();
		this.userId = userId;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.nickname = nickname;
		this.name = name;
		this.phone = phone;
		this.ssnFront = ssnFront;
		this.ssnBack = ssnBack;
		this.email = email;
		this.bizNo = bizNo;
		this.repName = repName;
		this.openDate = openDate;
		this.corpName = corpName;
		this.zipCode = zipCode;
		this.address = address;
		this.detailAddress = detailAddress;
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
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSsnFront() {
		return ssnFront;
	}
	public void setSsnFront(String ssnFront) {
		this.ssnFront = ssnFront;
	}
	public String getSsnBack() {
		return ssnBack;
	}
	public void setSsnBack(String ssnBack) {
		this.ssnBack = ssnBack;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
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
    
    
}
