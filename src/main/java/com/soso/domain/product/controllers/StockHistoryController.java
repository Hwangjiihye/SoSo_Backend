package com.soso.domain.product.controllers;

import com.soso.domain.product.dto.StockHistoryDTO;
import com.soso.domain.product.services.StockHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-history")
public class StockHistoryController {

    @Autowired
    private StockHistoryService stockHistoryService;

    /**
     * GET /api/stock-history/dashboard
     * 대시보드 메인 화면용 최신 5건 DTO 리스트 반환
     */
    @GetMapping("/dashboard")
    public ResponseEntity<List<StockHistoryDTO>> getDashboardHistory() {
        List<StockHistoryDTO> history = stockHistoryService.getDashboardHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * GET /api/stock-history/modal
     * 모달창용 페이징 데이터 반환 (page: 페이지 번호, size: 페이지당 항목 수)
     */
    @GetMapping("/modal")
    public ResponseEntity<Map<String, Object>> getModalHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
    	System.out.println(page);
        Map<String, Object> data = stockHistoryService.getModalHistory(page, size);
        return ResponseEntity.ok(data);
    }
}
