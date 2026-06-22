package com.soso.domain.employee.dto;

/**
 * [초보자 가이드 - 직원 정보 전달 객체 (EmployeeDTO)]
 * 
 * 개념: DB의 `employees` 테이블과 매핑되며, 직원 등록, 정보 조회, 상태 변경 시 
 *      필요한 회원 정보를 담고 이동시키는 데이터 가방(DTO) 역할을 합니다.
 */
public class EmployeeDTO {
    
    // 직원의 고유 식별 번호 (employees 테이블의 기본키)
    private int employeeSeq;
    
    // 점주(사업자)의 고유 식별 번호 (business_owners 테이블의 기본키와 연관)
    private int businessSeq;
    
    // 매장(가게)의 고유 식별 번호 (stores 테이블의 기본키와 연관)
    private int storeSeq;
    
    // 직원의 성명 (예: "홍길동")
    private String empName;
    
    // 직원의 휴대폰 번호 (예: "010-1234-5678")
    private String phone;
    
    // 정해진 근무 시작 시간 (예: "09:00")
    private String workStartTime;
    
    // 정해진 근무 종료 시간 (예: "18:00")
    private String workEndTime;
    
    // 직원의 현재 근무 상태 (예: "WORK" - 근무 중, "LEAVE" - 퇴근, "VACATION" - 휴가 등)
    private String status;

    /**
     * 기본 생성자
     * MyBatis 매핑 및 JSON 직렬화/역직렬화 시 자동으로 사용됩니다.
     */
    public EmployeeDTO() {}

    /**
     * 직원 고유 번호 Getter
     * @return 직원의 고유 일련번호
     */
    public int getEmployeeSeq() { return employeeSeq; }
    
    /**
     * 직원 고유 번호 Setter
     * @param employeeSeq 직원의 고유 일련번호
     */
    public void setEmployeeSeq(int employeeSeq) { this.employeeSeq = employeeSeq; }
    
    /**
     * 점주 고유 번호 Getter
     * @return 점주(사업자) 고유 번호
     */
    public int getBusinessSeq() { return businessSeq; }
    
    /**
     * 점주 고유 번호 Setter
     * @param businessSeq 점주(사업자) 고유 번호
     */
    public void setBusinessSeq(int businessSeq) { this.businessSeq = businessSeq; }
    
    /**
     * 매장 고유 번호 Getter
     * @return 매장 고유 번호
     */
    public int getStoreSeq() { return storeSeq; }
    
    /**
     * 매장 고유 번호 Setter
     * @param storeSeq 매장 고유 번호
     */
    public void setStoreSeq(int storeSeq) { this.storeSeq = storeSeq; }
    
    /**
     * 직원 성명 Getter
     * @return 직원 이름
     */
    public String getEmpName() { return empName; }
    
    /**
     * 직원 성명 Setter
     * @param empName 직원 이름
     */
    public void setEmpName(String empName) { this.empName = empName; }
    
    /**
     * 휴대폰 번호 Getter
     * @return 직원 휴대폰 번호
     */
    public String getPhone() { return phone; }
    
    /**
     * 휴대폰 번호 Setter
     * @param phone 직원 휴대폰 번호
     */
    public void setPhone(String phone) { this.phone = phone; }
    
    /**
     * 근무 시작 시간 Getter
     * @return 예정 근무 시작 시각
     */
    public String getWorkStartTime() { return workStartTime; }
    
    /**
     * 근무 시작 시간 Setter
     * @param workStartTime 예정 근무 시작 시각
     */
    public void setWorkStartTime(String workStartTime) { this.workStartTime = workStartTime; }
    
    /**
     * 근무 종료 시간 Getter
     * @return 예정 근무 종료 시각
     */
    public String getWorkEndTime() { return workEndTime; }
    
    /**
     * 근무 종료 시간 Setter
     * @param workEndTime 예정 근무 종료 시각
     */
    public void setWorkEndTime(String workEndTime) { this.workEndTime = workEndTime; }
    
    /**
     * 근무 상태 Getter
     * @return 직원의 현재 상태 ("WORK", "LEAVE" 등)
     */
    public String getStatus() { return status; }
    
    /**
     * 근무 상태 Setter
     * @param status 직원의 현재 상태 ("WORK", "LEAVE" 등)
     */
    public void setStatus(String status) { this.status = status; }
}
