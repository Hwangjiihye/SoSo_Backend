package com.soso.domain.groupbuy.services;

import com.soso.domain.groupbuy.dao.GroupBuyDAO;
import com.soso.domain.groupbuy.dto.GroupBuyDTO;
import com.soso.domain.groupbuy.dto.GroupBuyParticipantDTO;
import com.soso.domain.groupbuy.dto.ParticipantInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class GroupBuyService {

    @Autowired
    private GroupBuyDAO groupBuyDAO;

    // B. 공동구매 생성
    @Transactional
    public void createGroupBuy(GroupBuyDTO groupBuyDTO) {
        // 1. 공동구매 그룹 생성 (생성된 PK가 groupBuyDTO에 세팅됨)
        groupBuyDAO.insertGroupBuy(groupBuyDTO);
        
        // 2. 개설자를 참여자 테이블에 자동 등록
        GroupBuyParticipantDTO participantDTO = new GroupBuyParticipantDTO();
        participantDTO.setGroupBuySeq(groupBuyDTO.getGroupBuySeq());
        participantDTO.setUserSeq(groupBuyDTO.getUserSeq());
        
        groupBuyDAO.insertParticipant(participantDTO);
    }

    // C. 공동구매 목록 조회
    public List<GroupBuyDTO> getGroupBuys(String userType, Integer userSeq, String filter) {
        return groupBuyDAO.selectGroupBuys(userType, userSeq, filter);
    }

    // 내가 참여한 공동구매 목록 조회
    public List<GroupBuyDTO> getMyParticipatedGroups(int userSeq) {
        return groupBuyDAO.selectMyParticipatedGroups(userSeq);
    }

    // 내가 참여한 공동구매 중 완료(COMPLETED)된 목록 조회
    public List<GroupBuyDTO> getMyCompletedGroups(int userSeq) {
        return groupBuyDAO.selectMyCompletedGroups(userSeq);
    }

    // 내가 개설한 공동구매 목록 조회
    public List<GroupBuyDTO> getMyCreatedGroups(int userSeq) {
        return groupBuyDAO.selectMyCreatedGroups(userSeq);
    }

    // 내가 참여한 공동구매 개수 조회
    public int getMyParticipatedGroupsCount(int userSeq) {
        return groupBuyDAO.countMyParticipatedGroups(userSeq);
    }

    // 내가 개설한 완료된 공동구매 개수 조회
    public int getCompletedGroupBuysCount(int userSeq) {
        return groupBuyDAO.countCompletedGroupBuys(userSeq);
    }

    // 공동구매 단건 상세 조회
    public GroupBuyDTO getGroupBuyDetail(int groupBuySeq) {
        return groupBuyDAO.selectGroupBuyBySeq(groupBuySeq);
    }

    // D. 공동구매 참여 (더블 밸리데이션 핵심, Transaction 처리)
    @Transactional
    public void joinGroupBuy(int groupBuySeq, int userSeq) {
        // 1차 밸리데이션: 그룹 정보 획득 및 검증
        GroupBuyDTO targetGroup = groupBuyDAO.selectGroupBuyBySeq(groupBuySeq);
        
        if (targetGroup == null) {
            throw new IllegalArgumentException("존재하지 않는 공동구매입니다.");
        }
        
        if (!"RECRUITING".equals(targetGroup.getStatus())) {
            throw new IllegalStateException("모집 중인 공동구매가 아닙니다.");
        }

        if (targetGroup.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("모집 마감일이 지났습니다.");
        }
        
        if (targetGroup.getCurrentParticipants() >= targetGroup.getTargetParticipants()) {
            throw new IllegalStateException("모집 인원이 이미 마감되었습니다.");
        }

        // 2차 밸리데이션 및 처리: DB에서 원자적으로 +1 업데이트 실행. (실패 시 동시성으로 인한 마감)
        int updatedRows = groupBuyDAO.updateCurrentParticipantsCount(groupBuySeq);
        if (updatedRows == 0) {
            throw new IllegalStateException("동시 접속으로 인해 모집 인원이 마감되었습니다.");
        }

        // 참여자 정보 등록
        GroupBuyParticipantDTO participantDTO = new GroupBuyParticipantDTO();
        participantDTO.setGroupBuySeq(groupBuySeq);
        participantDTO.setUserSeq(userSeq);
        
        groupBuyDAO.insertParticipant(participantDTO);
    }

    // E. 공동구매 상태 변경 (거래처 전용)
    public void updateGroupBuyStatus(int groupBuySeq, String status, int userSeq) {
        int result = groupBuyDAO.updateGroupBuyStatus(groupBuySeq, status, userSeq);
        if (result == 0) {
            throw new IllegalArgumentException("상태 업데이트 권한이 없거나 존재하지 않는 그룹입니다.");
        }
    }

    // F. 참여 멤버 리스트 조회 (거래처용)
    public List<ParticipantInfoDTO> getParticipants(int groupBuySeq) {
        return groupBuyDAO.selectParticipantsByGroupBuySeq(groupBuySeq);
    }

    // [채팅 방어 로직] 해당 공동구매 채팅방 접근 권한 검증
    public boolean validateChatAccess(int groupBuySeq, int userSeq) {
        int count = groupBuyDAO.checkChatAccess(groupBuySeq, userSeq);
        return count > 0;
    }
    public List<GroupBuyDTO> getGroupBuyListByUser(int userSeq, Integer storeSeq) {
        return groupBuyDAO.getGroupBuyListByUser(userSeq, storeSeq);
    }
}
