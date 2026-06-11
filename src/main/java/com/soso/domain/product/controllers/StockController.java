package com.soso.domain.product.controllers;

import com.soso.domain.product.dto.*;
import com.soso.domain.product.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDTO>> list() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody StockDTO stock) {
        stockService.createStock(stock);
        return ResponseEntity.ok("품목이 등록되었습니다.");
    }

    @PostMapping("/incoming")
    public ResponseEntity<String> incoming(@RequestBody StockIncomingDTO incoming) {
        System.out.println(incoming.getQuantity());
        System.out.println(incoming.getStockSeq());
        System.out.println(incoming.getDetailStockName());
        System.out.println(incoming.getMemo());
        System.out.println(incoming.getExpirationDate());
    	try {
            stockService.processIncoming(incoming);
            return ResponseEntity.ok("입고 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/outbound")
    public ResponseEntity<String> outbound(@RequestBody StockOutboundRequest request) {
        try {
            stockService.processOutbound(request);
            return ResponseEntity.ok("출고 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/adjust")
    public ResponseEntity<String> adjust(@RequestBody StockAdjustRequest request) {
        try {
            stockService.processAdjust(request);
            return ResponseEntity.ok("재고 조정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{stockSeq}/batches")
    public ResponseEntity<List<StockBatchDTO>> getBatches(@PathVariable int stockSeq) {
        return ResponseEntity.ok(stockService.getBatches(stockSeq));
    }

    @GetMapping("/{stockSeq}/histories")
    public ResponseEntity<List<StockHistoryDTO>> getHistories(@PathVariable int stockSeq) {
        return ResponseEntity.ok(stockService.getHistories(stockSeq));
    }
}
