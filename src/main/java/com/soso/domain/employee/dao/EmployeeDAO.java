package com.soso.domain.employee.dao;

import com.soso.domain.employee.dto.AttendanceHistoryDTO;
import com.soso.domain.employee.dto.EmployeeDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [초보자 가이드 - 직원 근태 관리 DAO (Data Access Object)]
 * 
 * 개념: DB 테이블 `employees` 및 `attendance_history`에 접근하여 SQL을 질의합니다.
 *      MyBatis의 SqlSessionTemplate을 주입받아 매퍼 XML(`EmployeeMapper.xml`)에 선언된 쿼리를 기동시킵니다.
 */
@Repository
public class EmployeeDAO {

    // MyBatis 쿼리 실행을 지원하는 템플릿 객체 주입
    @Autowired
    private SqlSessionTemplate sqlSession;

    // MyBatis Mapper XML에 등록된 고유 namespace와 연결하기 위한 접두사
    private static final String NAMESPACE = "com.soso.mappers.employee.EmployeeMapper.";

    /**
     * [DAO 1] 특정 매장 소속 직원 전체 리스트 조회
     * 매핑 쿼리: EmployeeMapper.xml -> selectEmployeeList
     */
    public List<EmployeeDTO> selectEmployeeList(int businessSeq) {
        return sqlSession.selectList(NAMESPACE + "selectEmployeeList", businessSeq);
    }

    /**
     * [DAO 2] 신규 직원 인서트 등록
     * 매핑 쿼리: EmployeeMapper.xml -> insertEmployee
     */
    public void insertEmployee(EmployeeDTO employee) {
        sqlSession.insert(NAMESPACE + "insertEmployee", employee);
    }

    /**
     * [DAO 3] 직원 단건 고유번호로 상세 정보 조회
     * 매핑 쿼리: EmployeeMapper.xml -> selectEmployeeBySeq
     */
    public EmployeeDTO selectEmployeeBySeq(int employeeSeq) {
        return sqlSession.selectOne(NAMESPACE + "selectEmployeeBySeq", employeeSeq);
    }

    /**
     * [DAO 4] 직원의 현재 근무 상태 변경 (근무중, 퇴근, 휴가 등)
     * 매핑 쿼리: EmployeeMapper.xml -> updateEmployeeStatus
     */
    public void updateEmployeeStatus(int employeeSeq, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeSeq", employeeSeq);
        params.put("status", status);
        sqlSession.update(NAMESPACE + "updateEmployeeStatus", params);
    }

    /**
     * [DAO 5] 특정 직원의 월별 출퇴근 이력 리스트 조회
     * 매핑 쿼리: EmployeeMapper.xml -> selectAttendanceHistoryList
     */
    public List<AttendanceHistoryDTO> selectAttendanceHistoryList(int employeeSeq, String yearMonth) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeSeq", employeeSeq);
        params.put("yearMonth", yearMonth);
        return sqlSession.selectList(NAMESPACE + "selectAttendanceHistoryList", params);
    }

    /**
     * [DAO 6] 특정 직원의 오늘 날짜 출퇴근 이력 단건 조회
     * 매핑 쿼리: EmployeeMapper.xml -> selectTodayAttendance
     */
    public AttendanceHistoryDTO selectTodayAttendance(int employeeSeq, String workDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeSeq", employeeSeq);
        params.put("workDate", workDate);
        return sqlSession.selectOne(NAMESPACE + "selectTodayAttendance", params);
    }

    /**
     * [DAO 7] 직원 출근 시각 기록 삽입
     * 매핑 쿼리: EmployeeMapper.xml -> insertAttendance
     */
    public void insertAttendance(AttendanceHistoryDTO attendance) {
        sqlSession.insert(NAMESPACE + "insertAttendance", attendance);
    }

    /**
     * [DAO 8] 직원 퇴근 시각 및 근무시간 업데이트
     * 매핑 쿼리: EmployeeMapper.xml -> updateAttendanceCheckOut
     */
    public void updateAttendanceCheckOut(AttendanceHistoryDTO attendance) {
        sqlSession.update(NAMESPACE + "updateAttendanceCheckOut", attendance);
    }
}
