package com.soso.domain.notification.dao;

import com.soso.domain.notification.dto.NotificationDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.mappers.notification.NotificationMapper.";

    public void createTableIfNotExists() {
        sqlSession.update(NAMESPACE + "createTableIfNotExists");
    }

    public void insertNotification(NotificationDTO notification) {
        sqlSession.insert(NAMESPACE + "insertNotification", notification);
    }

    public List<NotificationDTO> selectRecentNotifications(int storeSeq) {
        return sqlSession.selectList(NAMESPACE + "selectRecentNotifications", storeSeq);
    }

    /**
     * [초보자 가이드]
     * 알림 읽음 처리 기능: 사용자가 알림을 클릭했을 때 데이터베이스의 notifications 테이블에서
     * 해당 알림의 읽음 컬럼(is_read)을 'Y'로 업데이트해줍니다.
     */
    public void updateNotificationRead(int notificationSeq) {
        sqlSession.update(NAMESPACE + "updateNotificationRead", notificationSeq);
    }

    /**
     * [초보자 가이드]
     * 매장 상호명 조회 기능: 주어진 매장 고유 번호(storeSeq)에 1:1로 매치되는
     * 상호명(company_name) 데이터를 데이터베이스에서 가져와 반환해줍니다.
     */
    public String selectCompanyNameByStoreSeq(int storeSeq) {
        return sqlSession.selectOne(NAMESPACE + "selectCompanyNameByStoreSeq", storeSeq);
    }

    /**
     * [초보자 가이드]
     * 유저의 대표 매장 번호 조회 기능: 유저 고유 시퀀스(userSeq)를 받아
     * 해당 유저가 소유한 매장 목록 중 가장 먼저 등록된 대표 store_seq 값을 조회합니다.
     */
    public Integer selectStoreSeqByUserSeq(long userSeq) {
        return sqlSession.selectOne(NAMESPACE + "selectStoreSeqByUserSeq", userSeq);
    }

    /**
     * [초보자 가이드]
     * 연계 거래처 매장 목록 조회 기능: 특정 가맹점 매장 번호(businessSeq)와 
     * 거래 관계(partner_relations)가 수립된 거래처(파트너)들의 매장 일련번호 목록을 리스트 형태로 가져옵니다.
     */
    public List<Integer> selectPartnerStoreSeqsByBusinessSeq(int businessSeq) {
        return sqlSession.selectList(NAMESPACE + "selectPartnerStoreSeqsByBusinessSeq", businessSeq);
    }

    /**
     * [초보자 가이드]
     * 매장 점주 유형 및 상호명 조회 기능: 주어진 매장 번호(storeSeq)의 
     * 주인 유저 유형(BUSINESS/PARTNER)과 해당 매장의 상호명(companyName)을 Map 형태로 묶어서 반환받습니다.
     */
    public java.util.Map<String, Object> selectStoreOwnerInfo(int storeSeq) {
        return sqlSession.selectOne(NAMESPACE + "selectStoreOwnerInfo", storeSeq);
    }

    /**
     * 알림 수신 동의 여부 체크
     */
    public String checkNotificationEnabled(int storeSeq, String notificationType) {
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("storeSeq", storeSeq);
        params.put("notificationType", notificationType);
        return sqlSession.selectOne(NAMESPACE + "checkNotificationEnabled", params);
    }
}
