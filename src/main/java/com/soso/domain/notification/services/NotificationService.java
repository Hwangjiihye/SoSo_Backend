package com.soso.domain.notification.services;

import com.soso.domain.notification.dao.NotificationDAO;
import com.soso.domain.notification.dto.NotificationDTO;
import com.soso.domain.notification.events.NotificationEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [초보자 가이드 - 알림 서비스 클래스]
 * 
 * 개념: Service는 애플리케이션의 핵심 '비즈니스 로직'이 흐르는 공간입니다. 
 *      DAO를 호출하여 DB를 조작하고, 웹소켓을 보내는 등의 조율 역할을 수행합니다.
 * 어노테이션 설명:
 *  - @Service: 이 클래스가 비즈니스 로직을 처리하는 스프링 서비스 컴포넌트임을 명시하여 스프링 컨테이너가 관리하게 합니다.
 */
@Service
public class NotificationService {

    // DB 접근 객체 주입
    @Autowired
    private NotificationDAO notificationDAO;

    // 스프링 STOMP 웹소켓의 메시지 전송 템플릿 주입 (지정 주소로 실시간 알림을 보낼 때 사용)
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * [테이블 자동 초기화 메소드]
     * 어노테이션 설명:
     *  - @PostConstruct: 스프링 빈(Bean) 객체가 완전히 생성되고 의존성 주입이 완료된 후 최초 1회 자동으로 실행되는 안성맞춤 어노테이션입니다.
     * 기능: 애플리케이션 시작 시 notifications 테이블이 혹시나 데이터베이스에 존재하지 않으면 자동으로 새롭게 생성해줍니다.
     */
    @PostConstruct
    public void init() {
        try {
            notificationDAO.createTableIfNotExists();
        } catch (Exception e) {
            System.err.println("[NotificationService] 테이블 초기화 실패: " + e.getMessage());
        }
    }

    /**
     * [알림 이벤트 리스너]
     * 
     * 어노테이션 설명:
     *  - @Async: 이 메소드를 메인 쓰레드가 아닌 백그라운드의 별도 '비동기 쓰레드'에서 즉시 독립적으로 실행시켜 줍니다.
     *            재고 차감이나 발주 행위를 완료한 원래의 쓰레드가 대기(Block)하지 않고 바로 응답할 수 있게 돕습니다.
     *  - @EventListener: 스프링 시스템 전체에 뿌려진 NotificationEvent 타입의 이벤트를 감시하고 있다가 감지되면 즉시 이 메소드를 가동시킵니다.
     *  - @Transactional: 하나의 작업 단위(트랜잭션)로 묶어서 실행합니다. 중간에 오류가 나면 롤백해줍니다.
     *    - propagation = Propagation.REQUIRES_NEW: 발주/재고 등의 기존 트랜잭션과 무관하게 독립적인 '새로운 트랜잭션'을 보장받습니다.
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleNotificationEvent(NotificationEvent event) {
        // 1. 이벤트 객체로부터 데이터 전달받아 저장용 DTO 바구니 조립
        NotificationDTO notification = new NotificationDTO(
            event.getStoreSeq(),
            event.getType(),
            event.getTitle(),
            event.getMessage()
        );
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead("N"); // 기본 안읽음으로 설정

        // 2. DB에 안전하게 인서트 저장
        notificationDAO.insertNotification(notification);

        // 3. 실시간 웹소켓(STOMP Broker)을 통한 화면 전송
        // - 대상 토픽 주소: "/sub/store/{storeSeq}/notifications"
        // - 이 주소를 리액트 프론트엔드가 구독(Subscribe)하고 있으므로 실시간 토스트 팝업이 작동하게 됩니다.
        String destination = "/sub/store/" + event.getStoreSeq() + "/notifications";
        messagingTemplate.convertAndSend(destination, notification);
    }

    /**
     * [최근 3일간의 알림 목록 가져오기]
     */
    public List<NotificationDTO> getRecentNotifications(int storeSeq) {
        return notificationDAO.selectRecentNotifications(storeSeq);
    }

    /**
     * [특정 알림 읽음 표시]
     */
    @Transactional
    public void markAsRead(int notificationSeq) {
        notificationDAO.updateNotificationRead(notificationSeq);
    }
}
