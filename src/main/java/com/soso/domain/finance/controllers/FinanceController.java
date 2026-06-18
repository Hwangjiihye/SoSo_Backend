package com.soso.domain.finance.controllers;

import com.soso.domain.finance.dto.FinanceDTO;
import com.soso.domain.finance.services.FinanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/list")
    public ResponseEntity<List<FinanceDTO>> getFinanceList(
            HttpServletRequest request,
            @RequestParam(required = false) Integer storeSeq,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String type) {
        
        Long userSeqLong = (Long) request.getAttribute("user_seq");
        int userSeq = userSeqLong.intValue();
        
        List<FinanceDTO> list = financeService.getFinanceList(userSeq, storeSeq, startDate, endDate, type);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/daily-summary")
    public ResponseEntity<List<Map<String, Object>>> getDailySummary(
            HttpServletRequest request,
            @RequestParam(required = false) Integer storeSeq,
            @RequestParam String yearMonth) {
        
        Long userSeqLong = (Long) request.getAttribute("user_seq");
        int userSeq = userSeqLong.intValue();
        
        List<Map<String, Object>> summary = financeService.getDailySummary(userSeq, storeSeq, yearMonth);
        return ResponseEntity.ok(summary);
    }
}
