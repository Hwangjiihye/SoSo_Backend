package com.soso.domain.mypage.dto;

import java.time.LocalDate;

/**
 * @file BusinessMultiProfileDTO.java
 * @description 사장님이 새로운 매장을 추가할 때 사용하는 데이터 가방(DTO)입니다.
 * 화면에서 입력한 매장 정보(상호명, 주소 등)를 서버로 전달할 때 사용합니다.
 */
public class BusinessMultiProfileDTO {
    // 🎯 [국세청 API 규격 맞춤] 필드명을 API 요구사항(b_no, start_dt 등)과 동일하게 설정합니다.
    private String b_nm;         // 매장 상호명 (API 규격: b_nm)
    private String b_no;         // 사업자 등록 번호 (API 규격: b_no)
    private String p_nm;         // 대표자 성명 (API 규격: p_nm)
    private String start_dt;      // 개업 일자 (API 규격: start_dt, YYYYMMDD 형식)

    // 추가적인 우리 시스템용 필드들
    private Integer zipcode;     // 우편번호
    private String address1;     // 기본 주소
    private String address2;     // 상세 주소
    private Long userSeq;        // 사장님 회원 번호
    private Long storeSeq;       // 생성된 매장 번호

    public BusinessMultiProfileDTO() {}

    // 게터와 세터들
    public String getB_nm() { return b_nm; }
    public void setB_nm(String b_nm) { this.b_nm = b_nm; }

    public String getB_no() { return b_no; }
    public void setB_no(String b_no) { this.b_no = b_no; }

    public String getP_nm() { return p_nm; }
    public void setP_nm(String p_nm) { this.p_nm = p_nm; }

    public String getStart_dt() { return start_dt; }
    public void setStart_dt(String start_dt) { this.start_dt = start_dt; }

    public Integer getZipcode() { return zipcode; }
    public void setZipcode(Integer zipcode) { this.zipcode = zipcode; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public Long getUserSeq() { return userSeq; }
    public void setUserSeq(Long userSeq) { this.userSeq = userSeq; }

    public Long getStoreSeq() { return storeSeq; }
    public void setStoreSeq(Long storeSeq) { this.storeSeq = storeSeq; }
}
