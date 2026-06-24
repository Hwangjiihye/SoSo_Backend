package com.soso.domain.groupbuy.controllers;

import com.soso.domain.groupbuy.dto.GroupBuyDTO;
import com.soso.domain.groupbuy.dto.ParticipantInfoDTO;
import com.soso.domain.groupbuy.services.GroupBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/group-buys")
public class GroupBuyController {

    @Autowired
    private GroupBuyService groupBuyService;

    // B. 공동구매 생성 API
    @PostMapping
    public ResponseEntity<?> createGroupBuy(@RequestBody GroupBuyDTO groupBuyDTO,
                                            @RequestAttribute("userSeq") Integer userSeq,
                                            @RequestAttribute("userType") String userType) {
        // 보안: 세션(RequestAttribute)에서 뽑은 userSeq를 무조건 강제 세팅
        groupBuyDTO.setUserSeq(userSeq);
        // 추가: 개설자 타입 세팅
        groupBuyDTO.setCreatorType(userType);
        
        groupBuyService.createGroupBuy(groupBuyDTO);
        
        return ResponseEntity.ok().body(Map.of("message", "공동구매가 성공적으로 등록되었습니다."));
    }

    // C. 공동구매 목록 조회 API (?filter=my)
    @GetMapping
    public ResponseEntity<List<GroupBuyDTO>> getGroupBuys(
            @RequestParam(required = false) String filter,
            @RequestAttribute("userType") String userType,
            @RequestAttribute("userSeq") Integer userSeq) {
        
        List<GroupBuyDTO> list = groupBuyService.getGroupBuys(userType, userSeq, filter);
        return ResponseEntity.ok(list);
    }

    // 내가 참여한 공동구매 목록 조회 API
    @GetMapping("/participated")
    public ResponseEntity<List<GroupBuyDTO>> getMyParticipatedGroups(
            @RequestAttribute("userSeq") Integer userSeq) {
        
        List<GroupBuyDTO> list = groupBuyService.getMyParticipatedGroups(userSeq);
        return ResponseEntity.ok(list);
    }

    // 내가 참여한 공동구매 개수 조회 API
    @GetMapping("/participated/count")
    public ResponseEntity<Map<String, Integer>> getMyParticipatedGroupsCount(
            @RequestAttribute("userSeq") Integer userSeq) {
        
        int count = groupBuyService.getMyParticipatedGroupsCount(userSeq);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // 공동구매 단건 상세 조회 API
    @GetMapping("/{groupBuySeq}")
    public ResponseEntity<GroupBuyDTO> getGroupBuyDetail(@PathVariable("groupBuySeq") int groupBuySeq) {
        GroupBuyDTO detail = groupBuyService.getGroupBuyDetail(groupBuySeq);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    // D. 공동구매 참여 API
    @PostMapping("/{groupBuySeq}/join")
    public ResponseEntity<?> joinGroupBuy(@PathVariable("groupBuySeq") int groupBuySeq,
                                          @RequestAttribute("userSeq") Integer userSeq) {
        try {
            groupBuyService.joinGroupBuy(groupBuySeq, userSeq);
            return ResponseEntity.ok().body(Map.of("message", "공동구매 참여가 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // E. 그룹 상태 변경 API
    @PatchMapping("/{groupBuySeq}/status")
    public ResponseEntity<?> updateGroupBuyStatus(@PathVariable("groupBuySeq") int groupBuySeq,
                                                  @RequestBody Map<String, String> requestBody,
                                                  @RequestAttribute("userSeq") Integer userSeq) {

        String status = requestBody.get("status");
        try {
            groupBuyService.updateGroupBuyStatus(groupBuySeq, status, userSeq);
            return ResponseEntity.ok().body(Map.of("message", "상태가 성공적으로 변경되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // F. 참여 멤버 및 배송 정보 리스트 조회 API
    @GetMapping("/{groupBuySeq}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable("groupBuySeq") int groupBuySeq,
                                             @RequestAttribute("userType") String userType) {
        if (!"PARTNER".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "거래처 권한이 필요합니다."));
        }
        
        List<ParticipantInfoDTO> participants = groupBuyService.getParticipants(groupBuySeq);
        return ResponseEntity.ok(participants);
    }
    @GetMapping("/history")
    public ResponseEntity<List<GroupBuyDTO>> getGroupBuyHistory(
            HttpServletRequest request,
            @RequestParam(required = false) Integer storeSeq) {
        Long userSeqLong = (Long) request.getAttribute("user_seq");
        if (userSeqLong == null) {
            return ResponseEntity.status(401).build();
        }
        int userSeq = userSeqLong.intValue();
        
        List<GroupBuyDTO> list = groupBuyService.getGroupBuyListByUser(userSeq, storeSeq);
        return ResponseEntity.ok(list);
    }
}
