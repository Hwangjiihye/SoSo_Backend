package com.soso.domain.groupbuy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soso.domain.groupbuy.dto.GroupBuyDTO;
import com.soso.domain.groupbuy.services.GroupBuyService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/group-buy")
public class GroupBuyController {

    @Autowired
    private GroupBuyService groupBuyService;

    @GetMapping("/history")
    public ResponseEntity<List<GroupBuyDTO>> getGroupBuyHistory(
            HttpServletRequest request,
            @RequestParam(required = false) Integer storeSeq) {
        Long userSeqLong = (Long) request.getAttribute("user_seq");
        int userSeq = userSeqLong.intValue();
        
        List<GroupBuyDTO> list = groupBuyService.getGroupBuyListByUser(userSeq, storeSeq);
        return ResponseEntity.ok(list);
    }
}
