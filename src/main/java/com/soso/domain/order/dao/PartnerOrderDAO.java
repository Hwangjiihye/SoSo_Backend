package com.soso.domain.order.dao;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.soso.domain.order.dto.PartnerOrderDetailDTO;
import com.soso.domain.order.dto.PartnerOrderListDTO;

/**
 * [거래처 전용 발주 관리 DAO]
 * 초보자 가이드: DAO(Data Access Object)는 직접적으로 DB에 접근하는 역할을 합니다.
 * MyBatis의 SqlSession을 사용하여 Mapper XML에 정의된 쿼리를 실행합니다.
 */
@Repository
public class PartnerOrderDAO {

    @Autowired
    private SqlSession session; // MyBatis와 대화하기 위한 객체

    /**
     * 거래처용 발주 목록 조회 (검색 및 필터 포함)
     * partnerOrder 매퍼의 selectOrderList 쿼리를 실행합니다.
     */
    public List<PartnerOrderListDTO> selectOrderList(Long sellerSeq, String keyword, String status) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("sellerSeq", sellerSeq);
        params.put("keyword", keyword);
        params.put("status", status);
        return session.selectList("partnerOrder.selectOrderList", params);
    }

    /**
     * 발주 상세 내역 조회
     * partnerOrder 매퍼의 selectOrderDetail 쿼리를 실행합니다.
     */
    public List<PartnerOrderDetailDTO> selectOrderDetail(Long orderSeq) {
        return session.selectList("partnerOrder.selectOrderDetail", orderSeq);
    }

    /**
     * 발주 상태 변경
     */
    public void updateOrderStatus(Long orderSeq, String status) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("orderSeq", orderSeq);
        params.put("status", status);
        session.update("partnerOrder.updateOrderStatus", params);
    }

    /**
     * 발주번호로 사업자(구매자)의 user_seq 조회
     */
    public Long selectBuyerSeq(Long orderSeq) {
        return session.selectOne("partnerOrder.selectBuyerSeq", orderSeq);
    }

    public int selectTodayNewOrdersCount(Long sellerSeq) {
        return session.selectOne("partnerOrder.selectTodayNewOrdersCount", sellerSeq);
    }

    public int selectShippingOrdersCount(Long sellerSeq) {
        return session.selectOne("partnerOrder.selectShippingOrdersCount", sellerSeq);
    }

    public int selectWaitingPaymentsAmount(Long sellerSeq) {
        return session.selectOne("partnerOrder.selectWaitingPaymentsAmount", sellerSeq);
    }

    public List<java.util.Map<String, Object>> selectMonthlySales(Long sellerSeq) {
        return session.selectList("partnerOrder.selectMonthlySales", sellerSeq);
    }

    public List<java.util.Map<String, Object>> selectMonthlyCollections(Long sellerSeq) {
        return session.selectList("partnerOrder.selectMonthlyCollections", sellerSeq);
    }

    public List<java.util.Map<String, Object>> selectBusinessSalesSummary(Long sellerSeq) {
        return session.selectList("partnerOrder.selectBusinessSalesSummary", sellerSeq);
    }

    public List<java.util.Map<String, Object>> selectGroupOrders(Long sellerSeq) {
        return session.selectList("partnerOrder.selectGroupOrders", sellerSeq);
    }
}
