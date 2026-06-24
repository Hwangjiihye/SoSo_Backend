package com.soso.domain.order.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.soso.domain.order.dao.PartnerOrderDAO;
import com.soso.domain.order.dao.OrderDAO;
import com.soso.domain.order.dto.PartnerOrderDetailDTO;
import com.soso.domain.order.dto.PartnerOrderListDTO;
import com.soso.domain.notification.events.NotificationEvent;
import org.springframework.context.ApplicationEventPublisher;

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
    private OrderDAO orderDAO;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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

        // 5. 알림 이벤트 발행
        try {
            Map<String, Object> orderInfo = orderDAO.findOrderInfoBySeq(orderSeq);
            if (orderInfo != null && buyerSeq != null) {
                String orderNo = String.valueOf(orderInfo.get("orderNo"));
                eventPublisher.publishEvent(new NotificationEvent(
                    this,
                    buyerSeq.intValue(),
                    "ORDER_STATUS",
                    "발주 상태 변경",
                    String.format("발주번호 [%s]의 %s", orderNo, getStatusMessage(status))
                ));
            }
        } catch (Exception e) {
            System.err.println("[PartnerOrderService] 알림 이벤트 발행 실패: " + e.getMessage());
        }
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

    /**
     * [초보자 가이드 - 거래처 대시보드 데이터 조회 서비스]
     * 기능: 로그인한 파트너(거래처) 사장님의 고유 ID(sellerSeq)를 기반으로
     *      실제 DB 데이터를 집계 및 가공하여 대시보드(KPI 카드, 매출/수금 차트, 미수금, 가맹점별 거래 현황, 공동구매 목록) 데이터를 한 묶음의 Map 객체로 구성합니다.
     */
    public Map<String, Object> getDashboardData(Long sellerSeq) {
        Map<String, Object> data = new HashMap<>();

        // [1단계] 상단 요약 카드용 숫자 데이터 집계
        // - todayNewOrders: 오늘 접수된 신규 가맹점 발주 개수
        // - shippingOrders: 거래처에서 현재 배송 중인 발주 개수
        // - waitingPayments: 가맹점에서 발주 완료하였으나 거래처로 아직 입금이 되지 않은 대기 금액 총합
        int todayNewOrders = partnerOrderDAO.selectTodayNewOrdersCount(sellerSeq);
        int shippingOrders = partnerOrderDAO.selectShippingOrdersCount(sellerSeq);
        int waitingPayments = partnerOrderDAO.selectWaitingPaymentsAmount(sellerSeq);

        data.put("todayNewOrders", todayNewOrders);
        data.put("shippingOrders", shippingOrders);
        data.put("waitingPayments", waitingPayments);

        // [2단계] 월별 매출(Sales) 및 수금(Collection) 현황 조회
        // - salesRaw: 월별 주문 총금액 (orders 테이블)
        // - collectionsRaw: 월별 결제 완료된 총금액 (payments 테이블)
        List<Map<String, Object>> salesRaw = partnerOrderDAO.selectMonthlySales(sellerSeq);
        List<Map<String, Object>> collectionsRaw = partnerOrderDAO.selectMonthlyCollections(sellerSeq);

        // [3단계] 최근 6개월 매출/수금 데이터를 달별로 병합(Merge)
        // - DB 조회 데이터에 특정 달의 거래 기록이 없더라도 차트의 가로축(1월~6월 등)이 유지되도록
        //   최근 6개월의 맵 틀을 기본 '0원'으로 세팅해 놓은 후, 데이터가 있는 월만 덮어씁니다.
        Map<String, Map<String, Object>> mergeMap = new java.util.LinkedHashMap<>();
        
        java.time.LocalDate now = java.time.LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            java.time.LocalDate targetDate = now.minusMonths(i);
            String monthName = targetDate.getMonthValue() + "월";
            Map<String, Object> mData = new HashMap<>();
            mData.put("month", monthName);
            mData.put("sales", 0);
            mData.put("collection", 0);
            mergeMap.put(monthName, mData);
        }

        // 실제 DB에서 가져온 월별 매출 데이터를 기본 맵에 병합
        if (salesRaw != null) {
            for (Map<String, Object> s : salesRaw) {
                String m = String.valueOf(s.get("month"));
                int amt = s.get("amount") != null ? ((Number) s.get("amount")).intValue() : 0;
                if (mergeMap.containsKey(m)) {
                    mergeMap.get(m).put("sales", amt);
                } else {
                    Map<String, Object> mData = new HashMap<>();
                    mData.put("month", m);
                    mData.put("sales", amt);
                    mData.put("collection", 0);
                    mergeMap.put(m, mData);
                }
            }
        }

        // 실제 값 병합 (수금)
        if (collectionsRaw != null) {
            for (Map<String, Object> c : collectionsRaw) {
                String m = String.valueOf(c.get("month"));
                int amt = c.get("amount") != null ? ((Number) c.get("amount")).intValue() : 0;
                if (mergeMap.containsKey(m)) {
                    mergeMap.get(m).put("collection", amt);
                } else {
                    Map<String, Object> mData = new HashMap<>();
                    mData.put("month", m);
                    mData.put("sales", 0);
                    mData.put("collection", amt);
                    mergeMap.put(m, mData);
                }
            }
        }

        List<Map<String, Object>> trendList = new java.util.ArrayList<>(mergeMap.values());
        data.put("monthlySalesAndCollections", trendList);

        // 3. 이번 달 매출 & 수금
        String currentMonthName = now.getMonthValue() + "월";
        int thisMonthSales = 0;
        int thisMonthCollections = 0;
        if (mergeMap.containsKey(currentMonthName)) {
            thisMonthSales = (int) mergeMap.get(currentMonthName).get("sales");
            thisMonthCollections = (int) mergeMap.get(currentMonthName).get("collection");
        }
        data.put("thisMonthSales", thisMonthSales);
        data.put("thisMonthCollections", thisMonthCollections);

        // 4. 누적 미수금 및 변동 추이 계산
        int totalReceivables = waitingPayments; // 실 미수금 합계 (주문은 넣었으나 결제 안된 금액)
        data.put("totalReceivables", totalReceivables);

        // 미수금 변동 추이 (월별 누적 매출 - 누적 수금)
        List<Map<String, Object>> receivableTrendList = new java.util.ArrayList<>();
        int accumSales = 0;
        int accumCollections = 0;
        for (Map<String, Object> m : trendList) {
            accumSales += (int) m.get("sales");
            accumCollections += (int) m.get("collection");
            int diff = accumSales - accumCollections;
            if (diff < 0) diff = 0; // 음수 방지

            Map<String, Object> trendItem = new HashMap<>();
            trendItem.put("month", m.get("month"));
            trendItem.put("amount", diff);
            receivableTrendList.add(trendItem);
        }
        data.put("receivableTrend", receivableTrendList);

        // 5. 사업자별 매출 요약 정보
        List<Map<String, Object>> bizSummaryRaw = partnerOrderDAO.selectBusinessSalesSummary(sellerSeq);
        List<Map<String, Object>> businessSales = new java.util.ArrayList<>();
        if (bizSummaryRaw != null) {
            for (Map<String, Object> b : bizSummaryRaw) {
                String name = String.valueOf(b.get("companyName"));
                int oCount = b.get("orderCount") != null ? ((Number) b.get("orderCount")).intValue() : 0;
                int totalSales = b.get("totalSales") != null ? ((Number) b.get("totalSales")).intValue() : 0;
                int receivable = b.get("receivableAmount") != null ? ((Number) b.get("receivableAmount")).intValue() : 0;
                
                int collected = totalSales - receivable;
                int progress = 100;
                if (totalSales > 0) {
                    progress = (int) (((double) collected / totalSales) * 100);
                }
                
                String desc = String.format("이번 달 %d건, 미수금 %s원", oCount, String.format("%,d", receivable));
                String amountStr = String.format("%,d원", totalSales);
                
                String color = "bg-emerald-500";
                String trend = "↑ 정상";
                if (progress < 40) {
                    color = "bg-red-500";
                    trend = "연체 위험";
                } else if (progress < 80) {
                    color = "bg-orange-400";
                    trend = "입금 필요";
                }

                Map<String, Object> bItem = new HashMap<>();
                bItem.put("name", name);
                bItem.put("desc", desc);
                bItem.put("amount", amountStr);
                bItem.put("trend", trend);
                bItem.put("progress", progress);
                bItem.put("color", color);
                businessSales.add(bItem);
            }
        }
        data.put("businessSales", businessSales);

        // 6. 공동 발주 현황
        List<Map<String, Object>> groupRaw = partnerOrderDAO.selectGroupOrders(sellerSeq);
        List<Map<String, Object>> groupOrders = new java.util.ArrayList<>();
        if (groupRaw != null) {
            for (Map<String, Object> g : groupRaw) {
                int id = g.get("id") != null ? ((Number) g.get("id")).intValue() : 0;
                String title = String.valueOf(g.get("title"));
                String status = String.valueOf(g.get("status"));
                int target = g.get("targetParticipants") != null ? ((Number) g.get("targetParticipants")).intValue() : 0;
                int current = g.get("currentParticipants") != null ? ((Number) g.get("currentParticipants")).intValue() : 0;
                
                int progress = 0;
                if (target > 0) {
                    progress = (int) (((double) current / target) * 100);
                }

                String statusKo = "모집 중";
                String btnText = "참여하기";
                String color = "bg-emerald-500";
                
                if ("PREPARING".equals(status)) {
                    statusKo = "발주 완료";
                    btnText = "상세 보기";
                    color = "bg-blue-500";
                } else if ("SHIPPING".equals(status)) {
                    statusKo = "배송 중";
                    btnText = "배송 조회";
                    color = "bg-violet-500";
                } else if ("DELIVERED".equals(status)) {
                    statusKo = "완료";
                    btnText = "이력 보기";
                    color = "bg-gray-500";
                } else if ("CANCELED".equals(status)) {
                    statusKo = "취소됨";
                    btnText = "확인";
                    color = "bg-red-500";
                }

                String dDayStr = "모집 중";
                if (g.get("endDate") != null) {
                    java.time.LocalDateTime endDate = (java.time.LocalDateTime) g.get("endDate");
                    long days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), endDate.toLocalDate());
                    if (days < 0) {
                        dDayStr = "마감 완료";
                    } else if (days == 0) {
                        dDayStr = "마감 오늘";
                    } else {
                        dDayStr = "마감 D-" + days;
                    }
                }

                Map<String, Object> gItem = new HashMap<>();
                gItem.put("id", id);
                gItem.put("title", title);
                gItem.put("status", statusKo);
                gItem.put("progress", progress);
                gItem.put("color", color);
                gItem.put("dDay", dDayStr);
                gItem.put("btn", btnText);
                groupOrders.add(gItem);
            }
        }
        data.put("groupOrders", groupOrders);

        return data;
    }
}
