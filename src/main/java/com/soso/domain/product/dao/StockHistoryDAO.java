package com.soso.domain.product.dao;

import com.soso.domain.product.dto.StockHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StockHistoryDAO {
    // 재고 변동 이력 기록
    int insertStockHistory(StockHistoryDTO history);

    // 대시보드 메인용 최신 5건 고정 조회
    List<StockHistoryDTO> getTop5StockHistory();

    // 팝업 모달창용 페이징 쿼리
    List<StockHistoryDTO> getStockHistoryWithPaging(@Param("offset") int offset, @Param("size") int size);

    // 전체 이력 개수 조회 (페이징용)
    int getTotalHistoryCount();
}
