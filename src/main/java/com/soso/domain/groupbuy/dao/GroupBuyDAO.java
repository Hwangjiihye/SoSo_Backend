package com.soso.domain.groupbuy.dao;

import com.soso.domain.groupbuy.dto.GroupBuyDTO;
import com.soso.domain.groupbuy.dto.GroupBuyParticipantDTO;
import com.soso.domain.groupbuy.dto.ParticipantInfoDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupBuyDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "com.soso.domain.groupbuy.dao.GroupBuyDAO.";

    // 공동구매 등록
    public void insertGroupBuy(GroupBuyDTO dto) {
        sqlSession.insert(NAMESPACE + "insertGroupBuy", dto);
    }

    // 공동구매 목록 조회 (권한 및 필터에 따른 동적 쿼리)
    public List<GroupBuyDTO> selectGroupBuys(String userType, Integer userSeq, String filter) {
        Map<String, Object> params = new HashMap<>();
        params.put("userType", userType);
        params.put("userSeq", userSeq);
        params.put("filter", filter);
        return sqlSession.selectList(NAMESPACE + "selectGroupBuys", params);
    }

    // 내가 참여한 공동구매 목록 조회
    public List<GroupBuyDTO> selectMyParticipatedGroups(int userSeq) {
        return sqlSession.selectList(NAMESPACE + "selectMyParticipatedGroups", userSeq);
    }

    // 단건 상세 조회 (참여 시 검증용)
    public GroupBuyDTO selectGroupBuyBySeq(int groupBuySeq) {
        return sqlSession.selectOne(NAMESPACE + "selectGroupBuyBySeq", groupBuySeq);
    }

    // 참여자 인원 증가 (동시성 및 초과 방지)
    public int updateCurrentParticipantsCount(int groupBuySeq) {
        return sqlSession.update(NAMESPACE + "updateCurrentParticipantsCount", groupBuySeq);
    }

    // 참여 내역 등록
    public void insertParticipant(GroupBuyParticipantDTO dto) {
        sqlSession.insert(NAMESPACE + "insertParticipant", dto);
    }

    // 개설자(거래처) 권한 검증 및 상태 변경
    public int updateGroupBuyStatus(int groupBuySeq, String status, int userSeq) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupBuySeq", groupBuySeq);
        params.put("status", status);
        params.put("userSeq", userSeq);
        return sqlSession.update(NAMESPACE + "updateGroupBuyStatus", params);
    }

    // 특정 공구의 참여 멤버 및 배송정보 리스트 조회
    public List<ParticipantInfoDTO> selectParticipantsByGroupBuySeq(int groupBuySeq) {
        return sqlSession.selectList(NAMESPACE + "selectParticipantsByGroupBuySeq", groupBuySeq);
    }

    // 채팅방 접근 권한 확인 로직 (개설자이거나 참여자인지 확인)
    public int checkChatAccess(int groupBuySeq, int userSeq) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupBuySeq", groupBuySeq);
        params.put("userSeq", userSeq);
        return sqlSession.selectOne(NAMESPACE + "checkChatAccess", params);
    }
}
