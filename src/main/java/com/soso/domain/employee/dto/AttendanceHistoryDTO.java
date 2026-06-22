package com.soso.domain.employee.dto;

/**
 * [초보자 가이드 - 직원 근태 이력 정보 전달 객체 (AttendanceHistoryDTO)]
 * 
 * 개념: DB의 `attendance_history`(근태 이력) 테이블 및 관련된 화면 데이터를 
 *      서버와 클라이언트(React UI) 간에 주고받기 위한 데이터 바구니(DTO)입니다.
 *      직원의 일자별 출퇴근 시각, 지각/조퇴 여부 상태 등을 담습니다.
 */
public class AttendanceHistoryDTO {
    
    // 근태 기록 일련번호 (기본키, DB에서 자동 생성됨)
    private int attendanceSeq;
    
    // 직원의 고유 식별 번호 (employees 테이블의 기본키와 매핑됨)
    private int employeeSeq;
    
    // 근무일자 (예: "2026-06-19")
    private String workDate;
    
    // 실제 출근한 시각 (예: "08:55:00")
    private String actualStartTime;
    
    // 실제 퇴근한 시각 (예: "18:05:00")
    private String actualEndTime;
    
    // 근태 상태 (예: "정상", "지각", "조퇴", "결근" 등)
    private String attendanceStatus;
    
    // 비고사항 또는 특이사항 (예: "정해진 시간보다 늦게 출근함", "조퇴 신청서 제출" 등)
    private String memo;
    
    // 데이터 생성 일시 (DB에 기록된 등록일자)
    private String createdAt;
    
    // 조인(Join) 쿼리 등을 통해 조회된 직원의 이름 (화면 표시용)
    private String empName;

    /**
     * 기본 생성자
     * MyBatis 매핑 및 직렬화 시 자동으로 호출됩니다.
     */
    public AttendanceHistoryDTO() {}

    /**
     * 근태 기록 일련번호 Getter
     * @return 근태 기록의 고유 번호
     */
    public int getAttendanceSeq() { return attendanceSeq; }
    
    /**
     * 근태 기록 일련번호 Setter
     * @param attendanceSeq 근태 기록의 고유 번호
     */
    public void setAttendanceSeq(int attendanceSeq) { this.attendanceSeq = attendanceSeq; }
    
    /**
     * 직원 고유 번호 Getter
     * @return 해당 근태 기록 대상 직원의 고유 식별 번호
     */
    public int getEmployeeSeq() { return employeeSeq; }
    
    /**
     * 직원 고유 번호 Setter
     * @param employeeSeq 해당 근태 기록 대상 직원의 고유 식별 번호
     */
    public void setEmployeeSeq(int employeeSeq) { this.employeeSeq = employeeSeq; }
    
    /**
     * 근무일자 Getter
     * @return 근무일자 (YYYY-MM-DD 형식)
     */
    public String getWorkDate() { return workDate; }
    
    /**
     * 근무일자 Setter
     * @param workDate 근무일자 (YYYY-MM-DD 형식)
     */
    public void setWorkDate(String workDate) { this.workDate = workDate; }
    
    /**
     * 실제 출근 시각 Getter
     * @return 실제 출근 시간 (HH:mm:ss 형식)
     */
    public String getActualStartTime() { return actualStartTime; }
    
    /**
     * 실제 출근 시각 Setter
     * @param actualStartTime 실제 출근 시간 (HH:mm:ss 형식)
     */
    public void setActualStartTime(String actualStartTime) { this.actualStartTime = actualStartTime; }
    
    /**
     * 실제 퇴근 시각 Getter
     * @return 실제 퇴근 시간 (HH:mm:ss 형식)
     */
    public String getActualEndTime() { return actualEndTime; }
    
    /**
     * 실제 퇴근 시각 Setter
     * @param actualEndTime 실제 퇴근 시간 (HH:mm:ss 형식)
     */
    public void setActualEndTime(String actualEndTime) { this.actualEndTime = actualEndTime; }
    
    /**
     * 근태 상태 Getter
     * @return 근태 결과 상태 ("정상", "지각", "조퇴" 등)
     */
    public String getAttendanceStatus() { return attendanceStatus; }
    
    /**
     * 근태 상태 Setter
     * @param attendanceStatus 근태 결과 상태 ("정상", "지각", "조퇴" 등)
     */
    public void setAttendanceStatus(String attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    
    /**
     * 메모 Getter
     * @return 특이사항 메모 내용
     */
    public String getMemo() { return memo; }
    
    /**
     * 메모 Setter
     * @param memo 특이사항 메모 내용
     */
    public void setMemo(String memo) { this.memo = memo; }
    
    /**
     * 데이터 등록 일시 Getter
     * @return 기록이 생성된 타임스탬프 문자열
     */
    public String getCreatedAt() { return createdAt; }
    
    /**
     * 데이터 등록 일시 Setter
     * @param createdAt 기록이 생성된 타임스탬프 문자열
     */
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    /**
     * 직원 이름 Getter
     * @return 화면용 직원 성명
     */
    public String getEmpName() { return empName; }
    
    /**
     * 직원 이름 Setter
     * @param empName 화면용 직원 성명
     */
    public void setEmpName(String empName) { this.empName = empName; }
}
