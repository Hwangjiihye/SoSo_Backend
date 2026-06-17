package com.soso.domain.product.controllers;

import com.soso.domain.product.dto.*;
import com.soso.domain.product.services.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 재고 목록 조회
     * @param storeSeq 프론트엔드 sessionStorage에서 전달받은 매장 번호
     */
    @GetMapping
    public ResponseEntity<List<StockDTO>> list(
            @RequestParam int storeSeq,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> filters = new HashMap<>();
        filters.put("search", search);
        filters.put("category", category);
        filters.put("status", status);
        filters.put("storeSeq", storeSeq);
        
        return ResponseEntity.ok(stockService.getStockList(filters));
    }

    /**
     * 품목 등록
     * @param stock DTO 내부에 storeSeq 포함됨
     */
    @PostMapping
    public ResponseEntity<String> register(@RequestParam int storeSeq,@RequestBody StockDTO stock) {
        stockService.createStock(storeSeq,stock);
        return ResponseEntity.ok("품목이 등록되었습니다.");
    }

    /**
     * 입고 처리
     * @param storeSeq 쿼리 파라미터로 전달받음
     */
    @PostMapping("/incoming")
    public ResponseEntity<String> incoming(
            @RequestParam int storeSeq,
            @RequestBody StockIncomingDTO incoming) {
    	try {
            stockService.processIncoming(incoming, storeSeq);
            return ResponseEntity.ok("입고 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 출고 처리
     */
    @PostMapping("/outbound")
    public ResponseEntity<String> outbound(
            @RequestParam int storeSeq,
            @RequestBody StockOutboundRequest requestObj) {
        try {
            stockService.processOutbound(requestObj, storeSeq);
            return ResponseEntity.ok("출고 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 재고 조정
     */
    @PostMapping("/adjust")
    public ResponseEntity<String> adjust(
            @RequestParam int storeSeq,
            @RequestBody StockAdjustRequest requestObj) {
        try {
            stockService.processAdjust(requestObj, storeSeq);
            return ResponseEntity.ok("재고 조정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 품목 정보 수정
     */
    @PutMapping("/{stockSeq}")
    public ResponseEntity<String> update(
            @PathVariable int stockSeq, 
            @RequestParam int storeSeq,
            @RequestBody StockDTO stock) {
        try {
            stock.setStockSeq(stockSeq);
            stock.setStoreSeq(storeSeq);
            stockService.updateStockInfo(stock);
            return ResponseEntity.ok("품목 정보가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 품목 삭제
     */
    @DeleteMapping("/{stockSeq}")
    public ResponseEntity<String> delete(
            @PathVariable int stockSeq,
            @RequestParam int storeSeq) {
        try {
            stockService.deleteStock(stockSeq, storeSeq);
            return ResponseEntity.ok("품목이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{stockSeq}/batches")
    public ResponseEntity<List<StockBatchDTO>> getBatches(
            @PathVariable int stockSeq,
            @RequestParam int storeSeq) {
        return ResponseEntity.ok(stockService.getBatches(stockSeq, storeSeq));
    }

    @GetMapping("/{stockSeq}/histories")
    public ResponseEntity<List<StockHistoryDTO>> getHistories(
            @PathVariable int stockSeq,
            @RequestParam int storeSeq) {
        return ResponseEntity.ok(stockService.getHistories(stockSeq, storeSeq));
    }

    @GetMapping("/countExpiringSoon")
    public ResponseEntity<Integer> getcountExpiringSoon(@RequestParam int storeSeq) {
        return ResponseEntity.ok(stockService.getcountExpiringSoon(storeSeq));
    }
}
