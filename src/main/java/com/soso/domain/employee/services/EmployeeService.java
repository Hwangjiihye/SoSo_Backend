package com.soso.domain.employee.services;

import com.soso.domain.employee.dao.EmployeeDAO;
import com.soso.domain.employee.dto.AttendanceHistoryDTO;
import com.soso.domain.employee.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * [초보자 가이드 - 직원 근태 관리 서비스 (EmployeeService)]
 * 
 * 역할: 직원 관리의 핵심 비즈니스 로직(비즈니스 룰)을 처리하는 곳입니다.
 *      예를 들어 출근 시각이 예정 시간보다 늦었을 때 '지각' 처리하는 계산 로직,
 *      이미 출근한 직원의 중복 출근 제한 로직, 퇴근 시 조퇴 판정 로직 등이 이곳에서 실행됩니다.
 *      `@Transactional`을 이용해 여러 DB 연산이 모두 성공하거나 모두 실패(롤백)하도록 묶어줍니다.
 */
@Service
public class EmployeeService {

    // DB 접근을 담당하는 DAO 주입
    @Autowired
    private EmployeeDAO employeeDAO;

    /**
     * [비즈니스 로직 1] 특정 매장의 직원 목록 조회
     * @param businessSeq 매장의 고유 번호
     * @return 소속 직원 DTO 리스트
     */
    public List<EmployeeDTO> getEmployeeList(int businessSeq) {
        // DAO를 통해 DB로부터 직원 리스트를 쿼리해서 리턴합니다.
        return employeeDAO.selectEmployeeList(businessSeq);
    }

    /**
     * [비즈니스 로직 2] 신규 직원 등록
     * @param employee 사장님이 입력한 직원 정보
     */
    public void registerEmployee(EmployeeDTO employee) {
        // 입력받은 정보 그대로 DB의 employees 테이블에 인서트합니다.
        employeeDAO.insertEmployee(employee);
    }

    /**
     * [비즈니스 로직 3] 직원 출근 처리
     * @param employeeSeq 출근 처리할 직원의 고유 번호
     * 
     * 동작 흐름:
     * 1. 직원이 존재하는지 검증합니다.
     * 2. 오늘 날짜로 이미 출근했는지 검증합니다. (하루에 두 번 출근 방지)
     * 3. 출근 예정 시간과 비교하여 1분 초과 시 '지각', 그 외에는 '정상'으로 근태 상태를 결정합니다.
     * 4. `attendance_history` 테이블에 출근 이력을 추가합니다.
     * 5. `employees` 테이블의 직원 상태를 "WORK"(근무중)으로 업데이트합니다.
     */
    @Transactional
    public void processCheckIn(int employeeSeq) {
        // 1. 직원 존재 검증
        EmployeeDTO emp = employeeDAO.selectEmployeeBySeq(employeeSeq);
        if (emp == null) {
            throw new RuntimeException("존재하지 않는 직원입니다.");
        }

        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();
        String todayStr = today.toString(); // "YYYY-MM-DD" 형태의 문자열

        // 2. 오늘 날짜 출근 이력이 이미 있는지 검사
        AttendanceHistoryDTO exist = employeeDAO.selectTodayAttendance(employeeSeq, todayStr);
        if (exist != null) {
            throw new RuntimeException("이미 오늘 출근 처리된 직원입니다.");
        }

        // 현재 시각 구하기
        LocalTime now = LocalTime.now();
        String nowStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 3. 지각 여부 계산
        String status = "정상";
        if (emp.getWorkStartTime() != null) {
            // 시간 형식이 HH:mm일 경우 :00을 붙여서 HH:mm:ss 포맷으로 맞춥니다.
            String startStr = emp.getWorkStartTime();
            if (startStr.length() == 5) startStr += ":00";
            
            // 직원의 출근 예정 시각 파싱
            LocalTime scheduledStart = LocalTime.parse(startStr);
            
            // 현재 시간이 예정 시간보다 1분 초과 시 지각으로 판정
            if (now.isAfter(scheduledStart.plusMinutes(1))) {
                status = "지각";
            }
        }

        // 4. 출근 근태 이력 엔티티(DTO) 생성 및 인서트
        AttendanceHistoryDTO attendance = new AttendanceHistoryDTO();
        attendance.setEmployeeSeq(employeeSeq);
        attendance.setWorkDate(todayStr);
        attendance.setActualStartTime(nowStr);
        attendance.setAttendanceStatus(status);
        // 지각인 경우 메모에 지각 이유를 자동 설정
        attendance.setMemo(status.equals("지각") ? "정해진 시간보다 늦게 출근함" : null);

        employeeDAO.insertAttendance(attendance);
        
        // 5. 직원의 현재 활동 상태를 'WORK'(근무중) 상태로 DB 업데이트
        employeeDAO.updateEmployeeStatus(employeeSeq, "WORK");
    }

    /**
     * [비즈니스 로직 4] 직원 퇴근 처리
     * @param employeeSeq 퇴근 처리할 직원의 고유 번호
     * 
     * 동작 흐름:
     * 1. 직원이 존재하는지 검증합니다.
     * 2. 오늘 날짜로 출근한 기록이 있는지 검증합니다. (출근 기록 없이 퇴근 불가)
     * 3. 이미 오늘 퇴근 처리가 완료된 상태인지 검증합니다. (중복 퇴근 방지)
     * 4. 퇴근 예정 시간보다 일찍 퇴근할 경우 기존 상태가 '정상'이었다면 '조퇴'로 변경합니다.
     * 5. 퇴근 시각(actualEndTime)과 근태 상태를 DB에 업데이트합니다.
     * 6. 직원의 상태를 'LEAVE'(퇴근) 상태로 변경합니다.
     */
    @Transactional
    public void processCheckOut(int employeeSeq) {
        // 1. 직원 존재 검증
        EmployeeDTO emp = employeeDAO.selectEmployeeBySeq(employeeSeq);
        if (emp == null) {
            throw new RuntimeException("존재하지 않는 직원입니다.");
        }

        LocalDate today = LocalDate.now();
        String todayStr = today.toString();

        // 2. 오늘 날짜의 출근 기록을 조회합니다.
        AttendanceHistoryDTO exist = employeeDAO.selectTodayAttendance(employeeSeq, todayStr);
        if (exist == null) {
            throw new RuntimeException("출근 기록이 없어 퇴근 처리를 할 수 없습니다.");
        }

        // 3. 중복 퇴근 처리 검증
        if (exist.getActualEndTime() != null) {
            throw new RuntimeException("이미 퇴근 처리 완료된 직원입니다.");
        }

        // 현재 퇴근 시각
        LocalTime now = LocalTime.now();
        String nowStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 4. 조퇴 여부 판정
        // 출근 당시 이미 지각했다면 상태는 '지각'을 유지하고, 정상 출근이었는데 일찍 퇴근하면 '조퇴'로 처리합니다.
        String status = exist.getAttendanceStatus();
        if (emp.getWorkEndTime() != null && "정상".equals(status)) {
            String endStr = emp.getWorkEndTime();
            if (endStr.length() == 5) endStr += ":00";
            
            // 직원의 정규 퇴근 예정 시각 파싱
            LocalTime scheduledEnd = LocalTime.parse(endStr);
            
            // 현재 시간이 예정 시간보다 이전이면 조퇴로 판정
            if (now.isBefore(scheduledEnd)) {
                status = "조퇴";
            }
        }

        // 5. 출근 이력 정보 업데이트 (퇴근 시간 기록 및 상태 수정)
        exist.setActualEndTime(nowStr);
        exist.setAttendanceStatus(status);
        exist.setMemo(status.equals("조퇴") ? "정해진 시간보다 일찍 퇴근함" : exist.getMemo());

        employeeDAO.updateAttendanceCheckOut(exist);
        
        // 6. 직원의 활동 상태를 'LEAVE'(퇴근)로 변경
        employeeDAO.updateEmployeeStatus(employeeSeq, "LEAVE");
    }

    /**
     * [비즈니스 로직 5] 특정 직원의 특정 년/월 근태 리스트 조회
     * @param employeeSeq 직원의 고유 식별 번호
     * @param yearMonth 조회할 년월 (예: "2026-06")
     * @return 월별 근태 이력 목록
     */
    public List<AttendanceHistoryDTO> getAttendanceHistory(int employeeSeq, String yearMonth) {
        // DAO를 통해 DB로부터 월 단위 조회 결과를 넘겨 받습니다.
        return employeeDAO.selectAttendanceHistoryList(employeeSeq, yearMonth);
    }
}
