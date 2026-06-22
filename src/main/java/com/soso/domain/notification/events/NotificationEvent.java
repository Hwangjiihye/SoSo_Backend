package com.soso.domain.notification.events;

import org.springframework.context.ApplicationEvent;

/**
 * [초보자 가이드 - 알림 이벤트 클래스]
 * 
 * 개념: 스프링 프레임워크가 제공하는 'ApplicationEvent'를 상속받은 커스텀 이벤트 클래스입니다.
 * 원리: 특정 비즈니스 로직(예: 발주 완료, 안전재고 부족)이 일어났을 때 이 이벤트를 던져주면(Publish),
 *      이벤트를 감시하고 있던 리스너(NotificationService)가 감지하여 알림을 비동기로 DB에 저장하고 소켓을 전송합니다.
 * 장점: 재고 관리나 주문 관리 코드가 알림 로직과 직접 얽히지 않게 설계(느슨한 결합, Loose Coupling)할 수 있습니다.
 */
public class NotificationEvent extends ApplicationEvent {
    
    // 알림을 수신할 대상 매장 일련번호 (stores.store_seq)
    private final int storeSeq;
    
    // 알림의 성격/유형 (예: SAFETY_LACK, NEW_ORDER, LATE_PAYMENT 등)
    private final String type;
    
    // 알림창에 굵게 노출될 제목 (예: "안전재고 부족 알림", "신규 발주서 도착")
    private final String title;
    
    // 알림창 하단에 상세 노출될 설명 메시지 (예: "[닭고기] 재고가 기준 미달입니다.")
    private final String message;

    /**
     * [이벤트 생성자]
     * @param source 이벤트를 최초로 던진 주체 객체 (보통 'this'를 넘깁니다)
     * @param storeSeq 수신 대상 매장 일련번호
     * @param type 알림 유형 코드
     * @param title 알림 제목
     * @param message 알림 상세 정보
     */
    public NotificationEvent(Object source, int storeSeq, String type, String title, String message) {
        super(source); // 부모 클래스인 ApplicationEvent의 생성자를 호출하여 이벤트를 초기화합니다.
        this.storeSeq = storeSeq;
        this.type = type;
        this.title = title;
        this.message = message;
    }

    // 외부(NotificationService 등)에서 알림 정보를 읽을 수 있도록 제공하는 Getter 메소드들입니다.
    public int getStoreSeq() { return storeSeq; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
}
