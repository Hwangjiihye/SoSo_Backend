package com.soso.domain.employee.controllers;

import com.soso.domain.employee.dto.AttendanceHistoryDTO;
import com.soso.domain.employee.dto.EmployeeDTO;
import com.soso.domain.employee.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [초보자 가이드 - 직원 근태 관리 API 컨트롤러]
 * 
 * 기능: 소상공인 매장에서 직원을 조회/등록하고, 매일 출근/퇴근을 처리하며,
 *      월별 출퇴근 기록(근태 이력)을 조회하는 HTTP API 게이트웨이 역할을 합니다.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    // 직원 관리 서비스 주입
    @Autowired
    private EmployeeService employeeService;

    /**
     * [API 1] 특정 매장 소속 직원 목록 조회
     * 요청 주소: GET http://localhost/api/employees?storeSeq=매장번호
     * 
     * 기능: React의 직원 관리 탭이 열릴 때, 해당 매장(storeSeq)에 등록된 직원들의 전체 목록을 DB에서 조회해 옵니다.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployeeList(@RequestParam int storeSeq) {
        List<EmployeeDTO> list = employeeService.getEmployeeList(storeSeq);
        return ResponseEntity.ok(list);
    }

    /**
     * [API 2] 신규 직원 등록
     * 요청 주소: POST http://localhost/api/employees
     * Body: EmployeeDTO 객체 (이름, 휴대전화, 시급 등)
     * 
     * 기능: 사장님이 모달 창에서 직원 정보를 입력한 후 '등록'을 누르면 DB의 employees 테이블에 인서트 처리를 수행합니다.
     */
    @PostMapping
    public ResponseEntity<String> registerEmployee(@RequestBody EmployeeDTO employee) {
        try {
            employeeService.registerEmployee(employee);
            return ResponseEntity.ok("직원 등록이 완료되었습니다.");
        } catch (Exception e) {
            // 중복된 휴대전화 번호 등 등록 오류 시 400 Bad Request 에러 메시지를 반환합니다.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [API 3] 직원 출근 처리
     * 요청 주소: POST http://localhost/api/employees/직원번호/check-in
     * 
     * 기능: 출근 모달창에서 특정 직원을 선택하고 '출근' 버튼을 클릭하면, 오늘 날짜로 attendance_history 테이블에 출근 레코드를 생성합니다.
     */
    @PostMapping("/{employeeSeq}/check-in")
    public ResponseEntity<String> checkIn(@PathVariable int employeeSeq) {
        try {
            employeeService.processCheckIn(employeeSeq);
            return ResponseEntity.ok("출근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [API 4] 직원 퇴근 처리
     * 요청 주소: POST http://localhost/api/employees/직원번호/check-out
     * 
     * 기능: 퇴근할 때 '퇴근' 버튼을 클릭하면 오늘 생성된 출근 레코드를 찾아 퇴근 시각(check_out_time)을 업데이트하고 근무 시간을 연산합니다.
     */
    @PostMapping("/{employeeSeq}/check-out")
    public ResponseEntity<String> checkOut(@PathVariable int employeeSeq) {
        try {
            employeeService.processCheckOut(employeeSeq);
            return ResponseEntity.ok("퇴근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * [API 5] 특정 직원의 월별 근태 이력 조회
     * 요청 주소: GET http://localhost/api/employees/직원번호/attendance?yearMonth=2026-06
     * 
     * 기능: 특정 직원의 상세 근태 이력을 누적 월별로 캘린더나 테이블 형태로 보고서 출력 시 데이터를 전달해줍니다.
     */
    @GetMapping("/{employeeSeq}/attendance")
    public ResponseEntity<List<AttendanceHistoryDTO>> getAttendanceHistory(
            @PathVariable int employeeSeq,
            @RequestParam String yearMonth) {
        List<AttendanceHistoryDTO> list = employeeService.getAttendanceHistory(employeeSeq, yearMonth);
        return ResponseEntity.ok(list);
    }
}
