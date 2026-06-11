package com.soso.domain.order.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.soso.domain.order.dto.PartnerOrderDetailDTO;
import com.soso.domain.order.dto.PartnerOrderListDTO;
import com.soso.domain.order.services.PartnerOrderService;

/**
 * [거래처 전용 발주 관리 컨트롤러]
 * 초보자 가이드: Controller는 외부(프론트엔드)에서 들어오는 요청을 받는 '문지기'입니다.
 * URL을 보고 어떤 서비스를 호출할지 결정합니다.
 */
@RestController
@RequestMapping("/api/partner/orders") // 이 컨트롤러의 모든 API는 /api/partner/orders 로 시작합니다.
public class PartnerOrderController {

    @Autowired
    private PartnerOrderService partnerOrderService; // 서비스 심부름꾼 주입

    /**
     * [API 1] 거래처용 발주 목록 조회 (검색 및 필터 포함)
     * 요청 예: GET /api/partner/orders?sellerSeq=10&keyword=가게&status=SHIPPING
     */
    @GetMapping
    public ResponseEntity<List<PartnerOrderListDTO>> getOrderList(
            @RequestParam Long sellerSeq,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        
        List<PartnerOrderListDTO> list = partnerOrderService.getOrderList(sellerSeq, keyword, status);
        return ResponseEntity.ok(list);
    }

    /**
     * [API 2] 발주 상세 내역 조회
     * 요청 예: GET /api/partner/orders/5
     * @param orderSeq 조회하고 싶은 발주서의 고유 번호 (URL 경로에서 가져옴)
     */
    @GetMapping("/{orderSeq}")
    public ResponseEntity<List<PartnerOrderDetailDTO>> getOrderDetail(@PathVariable Long orderSeq) {
        // 서비스에게 해당 발주서의 상세 내용을 가져오라고 시킵니다.
        List<PartnerOrderDetailDTO> details = partnerOrderService.getOrderDetail(orderSeq);
        
        // 상세 리스트를 프론트엔드에게 돌려줍니다.
        return ResponseEntity.ok(details);
    }

    /**
     * [API 3] 발주 상태 변경 (접수완료, 배송중 등)
     * 요청 예: PUT /api/partner/orders/5/status
     * Body 예: "SHIPPING"
     */
    @PutMapping("/{orderSeq}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long orderSeq, @RequestBody String status) {
        // 큰따옴표가 포함되어 들어올 수 있으므로 제거해줍니다.
        String cleanStatus = status.replace("\"", "");
        partnerOrderService.updateOrderStatus(orderSeq, cleanStatus);
        return ResponseEntity.ok("상태가 변경되었습니다.");
    }
}
