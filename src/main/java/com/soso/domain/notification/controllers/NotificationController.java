package com.soso.domain.notification.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.notification.dto.NotificationDTO;
import com.soso.domain.notification.services.NotificationService;

/**
 * [초보자 가이드 - 알림 API 컨트롤러]
 * 
 * 개념: Controller는 프론트엔드(React)에서 온 HTTP 요청(URL)을 제일 먼저 받아서 
 *      적절한 서비스(Service)에 전달하는 '문지기(라우터)' 역할을 수행합니다.
 * 어노테이션 설명:
 *  - @RestController: 이 클래스가 데이터를 JSON 형태로 프론트엔드에 바로 반환하는 RESTful 컨트롤러임을 선언합니다.
 *  - @RequestMapping("/api/notifications"): 이 컨트롤러 내부의 모든 API 주소 앞에 "/api/notifications"를 붙입니다.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    // 알림 비즈니스 로직을 처리하는 서비스를 스프링 컨테이너로부터 자동 주입(Autowired)받습니다.
    @Autowired
    private NotificationService notificationService;

    /**
     * [API 1] 최근 3일간의 알림 목록 조회
     * 요청 주소: GET http://localhost/api/notifications/recent?storeSeq=매장번호
     * 
     * 어노테이션 및 파라미터 설명:
     *  - @GetMapping("/recent"): HTTP GET 방식으로 "/recent" 요청이 올 때 이 메소드를 실행시킵니다.
     *  - @RequestParam int storeSeq: URL 쿼리 파라미터(예: ?storeSeq=9)의 값을 변수에 대입해옵니다.
     *  - ResponseEntity: HTTP 상태코드(200 OK 등)와 데이터를 한 바구니에 담아 보내기 위한 스프링 규격 리턴 타입입니다.
     */
    @GetMapping("/recent")
    public ResponseEntity<List<NotificationDTO>> getRecentNotifications(@RequestParam int storeSeq) {
        // 서비스에게 해당 매장의 최근 3일 알림을 가져오라고 지시합니다.
        List<NotificationDTO> list = notificationService.getRecentNotifications(storeSeq);
        // 200 OK 상태코드와 함께 알림 리스트 데이터를 리턴합니다.
        return ResponseEntity.ok(list);
    }

    /**
     * [API 2] 알림 읽음 처리
     * 요청 주소: PATCH http://localhost/api/notifications/알림고유번호/read
     * 
     * 어노테이션 및 파라미터 설명:
     *  - @PatchMapping("/{notificationSeq}/read"): 리소스의 일부(읽음 상태 컬럼)를 수정할 때 주로 PATCH 방식을 씁니다.
     *  - @PathVariable int notificationSeq: URL 경로 속에 포함된 변수값(예: /12/read 라면 12)을 파라미터로 바인딩합니다.
     */
    @PatchMapping("/{notificationSeq}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable int notificationSeq) {
        // 서비스에게 해당 알림 시퀀스를 읽음(Y)으로 바꾸라고 명령합니다.
        notificationService.markAsRead(notificationSeq);
        // 수정을 성공하고 반환할 바디 데이터가 없으므로 build()를 써서 빈 Response(200 OK)를 전달합니다.
        return ResponseEntity.ok().build();
    }
}
