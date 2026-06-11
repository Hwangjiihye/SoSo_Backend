package com.soso.domain.order.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.soso.domain.order.dao.PartnerOrderDAO;
import com.soso.domain.order.dto.PartnerOrderDetailDTO;
import com.soso.domain.order.dto.PartnerOrderListDTO;

/**
 * [거래처 전용 발주 관리 서비스]
 * 초보자 가이드: Service는 '비즈니스 로직'을 처리하는 곳입니다.
 * 지금은 단순 조회지만, 나중에는 '재고 차감'이나 '알림 발송' 같은 복잡한 일을 여기서 처리하게 됩니다.
 */
@Service
public class PartnerOrderService {

    @Autowired
    private PartnerOrderDAO partnerOrderDAO; // DAO 심부름꾼 주입

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // 웹소켓 알림을 보내는 도구

    /**
     * 거래처 사장님 앞으로 들어온 발주 목록을 가져옵니다. (검색 및 필터 포함)
     */
    public List<PartnerOrderListDTO> getOrderList(Long sellerSeq, String keyword, String status) {
        return partnerOrderDAO.selectOrderList(sellerSeq, keyword, status);
    }

    /**
     * 선택한 발주서의 세부 품목 리스트를 가져옵니다.
     */
    public List<PartnerOrderDetailDTO> getOrderDetail(Long orderSeq) {
        return partnerOrderDAO.selectOrderDetail(orderSeq);
    }

    /**
     * [발주 상태 변경 및 실시간 알림]
     * 거래처가 상태를 바꾸면 DB를 업데이트하고, 해당 사업자에게 웹소켓으로 알림을 보냅니다.
     */
    public void updateOrderStatus(Long orderSeq, String status) {
        // 1. DB 상태 업데이트
        partnerOrderDAO.updateOrderStatus(orderSeq, status);

        // 2. 이 주문을 넣은 사업자(buyer)의 번호를 찾음
        Long buyerSeq = partnerOrderDAO.selectBuyerSeq(orderSeq);

        // 3. 웹소켓으로 보낼 데이터 포장
        Map<String, Object> message = new HashMap<>();
        message.put("orderSeq", orderSeq);
        message.put("status", status);
        message.put("message", getStatusMessage(status));

        // 4. 사업자가 구독 중인 주소(/sub/order/사업자번호)로 전송
        messagingTemplate.convertAndSend("/sub/order/" + buyerSeq, (Object) message);
    }

    // 상태값에 따른 한글 메시지 변환
    private String getStatusMessage(String status) {
        switch (status) {
            case "ACCEPTED": return "발주가 접수완료되었습니다.";
            case "PREPARING": return "상품준비가 시작되었습니다.";
            case "SHIPPING": return "상품이 배송중입니다.";
            case "DELIVERED": return "배송이 완료되었습니다.";
            default: return "발주 상태가 변경되었습니다.";
        }
    }
}
