package com.soso.domain.notification.dto;

import java.time.LocalDateTime;

/**
 * [초보자 가이드 - 알림 DTO (Data Transfer Object)]
 * 
 * 개념: DTO는 계층 간에 데이터를 이동시키기 위해 사용하는 순수한 '상자' 같은 자바 객체입니다.
 *      여기에 어떠한 핵심 비즈니스 로직(행위)도 포함되지 않으며, 변수들과 그 변수를 넣고 꺼내는 Getter/Setter 메소드로만 이루어집니다.
 * 용도: 데이터베이스의 'notifications' 테이블 구조와 매핑되어, MyBatis Mapper를 통해 
 *      DB에 데이터를 넣거나 꺼내올 때 데이터 바구니 역할을 합니다.
 */
public class NotificationDTO {
    // 알림 테이블의 기본키 (Auto-Increment로 지정된 자동 증가 번호)
    private int notificationSeq;
    
    // 알림을 받는 대상 사업장 고유 ID
    private int storeSeq;
    
    // 알림의 성격 코드 (SAFETY_LACK, EXPIRY_IMMINENT, NEW_ORDER, LATE_PAYMENT 등)
    private String type;
    
    // 알림 제목
    private String title;
    
    // 알림 메시지 본문 내용
    private String message;
    
    // 사용자가 이 알림을 클릭해 읽었는지 여부 ('N': 안읽음, 'Y': 읽음)
    private String isRead;
    
    // 알림이 최초로 등록/발생한 시각
    private LocalDateTime createdAt;

    /**
     * [기본 생성자]
     * MyBatis 같은 라이브러리가 DB 조회 결과를 DTO 객체로 자동 변환(Mapping)할 때 사용하므로 반드시 정의해두어야 합니다.
     */
    public NotificationDTO() {}

    /**
     * [매개변수가 있는 편의용 생성자]
     * 비즈니스 로직에서 새로운 알림 객체를 한 번에 쉽게 조립하여 생성하기 위한 용도로 만듭니다.
     */
    public NotificationDTO(int storeSeq, String type, String title, String message) {
        this.storeSeq = storeSeq;
        this.type = type;
        this.title = title;
        this.message = message;
    }

    // 아래의 메소드들은 캡슐화된 private 필드들에 안전하게 접근하고 값을 변경하기 위해 필수적인 Getter / Setter 메소드들입니다.
    public int getNotificationSeq() { return notificationSeq; }
    public void setNotificationSeq(int notificationSeq) { this.notificationSeq = notificationSeq; }
    
    public int getStoreSeq() { return storeSeq; }
    public void setStoreSeq(int storeSeq) { this.storeSeq = storeSeq; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getIsRead() { return isRead; }
    public void setIsRead(String isRead) { this.isRead = isRead; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
