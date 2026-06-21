package com.soso.domain.payment.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.payment.dto.AutoPaymentScheduleDTO;
import com.soso.domain.payment.dto.PaymentCardDTO;
import com.soso.domain.payment.dto.PaymentDTO;

@Repository
public class PaymentDAO {
	
	@Autowired
	private SqlSessionTemplate mybatis;
	
	// 계좌 등록
	public int insertAccount(PaymentDTO dto) {
		return mybatis.insert("payment.account", dto);
	}
	
	// 계좌 출력
	public List<PaymentDTO> accountList(Long storeSeq) {
		return mybatis.selectList("payment.list", storeSeq);
	}
	
	// 계좌 등록 4개 제한
	public int accountCount(Long storeSeq) {
		return mybatis.selectOne("payment.limit", storeSeq);
	}
	
	// 계좌 삭제 처리: 실제 삭제 X -> Update
	public int accountDel(Long accountSeq) {
		return mybatis.update("payment.accountDel", accountSeq);
	}
	
	// 자동이체 설정 등록
	public int autoPaymentSchedule(AutoPaymentScheduleDTO dto) {
		return mybatis.insert("payment.autoSchedule", dto);
	}
	
	// 카드 등록
	public int insertCard(PaymentCardDTO dto) {
		return mybatis.insert("payment.insertCard", dto);
	}
	
	// 카드 조회
	public List<PaymentCardDTO> selectCard(Long storeSeq) {
		return mybatis.selectList("payment.selectCard", storeSeq);
	}
	
	/**
	 * 결제에 사용할 카드 1개 조회
	 *
	 * storeSeq: 현재 로그인한 사업자의 매장 번호
	 * cardSeq: 사용자가 결제에 선택한 카드 번호
	 *
	 * payment_cards 테이블에서 billing_key를 가져오기 위해 사용함
	 */
	public PaymentCardDTO selectCardForPayment(Integer storeSeq, Integer cardSeq) {

	    // MyBatis에 파라미터를 여러 개 넘기기 위해 Map 사용
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);
	    params.put("cardSeq", cardSeq);

	    // payment.xml의 selectCardForPayment 쿼리 실행
	    return mybatis.selectOne("payment.selectCardForPayment", params);
	}
	
		
	//	결제 대상 발주 목록 조회
	//	storeSeq = 돈을 내는 사업자 매장 번호
	//	partnerSeq = 돈을 받는 거래처 매장 번호
	//	orderSeqList = 사용자가 선택한 발주 번호 목록
	//	이미 결제된 발주는 제외하고, 실제로 결제 가능한 발주만 조회
	public List<Map<String, Object>> selectOrdersForPayment(
	        Integer storeSeq,
	        Integer partnerSeq,
	        List<Integer> orderSeqList) {

	    // MyBatis에 여러 값을 넘기기 위해 Map 사용
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);
	    params.put("partnerSeq", partnerSeq);
	    params.put("orderSeqList", orderSeqList);

	    // payment.xml의 selectOrdersForPayment 쿼리 실행
	    return mybatis.selectList("payment.selectOrdersForPayment", params);
	}
	
	
	 // 결제관리 최근 결제 내역 조회
	 // storeSeq = 현재 로그인한 사업자 매장 번호
	 // 이 화면은 내가 결제한 내역을 보여주는 곳이므로 payments.store_seq 기준으로 조회한다.
	public List<Map<String, Object>> selectRecentPayments(Integer storeSeq) {
	    return mybatis.selectList("payment.selectRecentPayments", storeSeq);
	}
	
	
	/**
	 * payments 테이블에 결제 내역 저장
	 *
	 * 포트원 결제가 성공한 뒤
	 * 우리 DB에 결제 결과를 남기기 위한 메서드
	 */
	public Integer insertPayment(
	        Integer storeSeq,
	        Integer partnerSeq,
	        Integer cardSeq,
	        String paymentId,
	        Integer totalAmount) {

	    // MyBatis에 여러 파라미터를 넘기기 위해 Map 사용
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);       // 돈을 낸 사업자 매장 번호
	    params.put("partnerSeq", partnerSeq);   // 돈을 받은 거래처 매장 번호
	    params.put("cardSeq", cardSeq);         // 결제에 사용한 카드 번호
	    params.put("paymentId", paymentId);     // 포트원 결제 ID
	    params.put("totalAmount", totalAmount); // 총 결제 금액

	    // payment.xml의 insertPayment 실행
	    // useGeneratedKeys로 생성된 payment_seq가 params.paymentSeq에 들어옴
	    mybatis.insert("payment.insertPayment", params);

	    // 생성된 payment_seq 반환
	    return ((Number) params.get("paymentSeq")).intValue();
	}


	/**
	 * payment_order_map 테이블에 결제한 발주 목록 저장
	 *
	 * 결제 1건과 발주 여러 건을 연결하기 위한 메서드
	 */
	public int insertPaymentOrderMap(Integer paymentSeq, List<Integer> orderSeqList) {

	    // MyBatis에 paymentSeq와 orderSeqList를 같이 넘김
	    Map<String, Object> params = new HashMap<>();
	    params.put("paymentSeq", paymentSeq);       // payments.payment_seq
	    params.put("orderSeqList", orderSeqList);   // 결제한 발주 번호 목록

	    // payment.xml의 insertPaymentOrderMap 실행
	    return mybatis.insert("payment.insertPaymentOrderMap", params);
	}


	/**
	 * expenses 테이블에 발주 결제 내역을 식자재비로 자동 저장
	 *
	 * 사업자 입장에서는 발주 결제가 지출이므로
	 * 지출 관리에도 자동 반영하기 위한 메서드
	 */
	public int insertExpensesForPaidOrders(Integer storeSeq, List<Integer> orderSeqList) {

	    // MyBatis에 현재 매장 번호와 결제한 발주 목록 전달
	    Map<String, Object> params = new HashMap<>();
	    params.put("storeSeq", storeSeq);           // 지출이 발생한 사업자 매장 번호
	    params.put("orderSeqList", orderSeqList);   // 지출로 등록할 발주 번호 목록

	    // payment.xml의 insertExpensesForPaidOrders 실행
	    return mybatis.insert("payment.insertExpensesForPaidOrders", params);
	}

}
