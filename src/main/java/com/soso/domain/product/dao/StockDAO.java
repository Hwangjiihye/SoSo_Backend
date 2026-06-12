package com.soso.domain.product.dao;

import com.soso.domain.product.dto.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StockDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.mappers.product.StockMapper.";

    public List<StockDTO> selectStockList() {
        return sqlSession.selectList(NAMESPACE + "selectStockList");
    }

    public void insertStock(StockDTO stock) {
        sqlSession.insert(NAMESPACE + "insertStock", stock);
    }

    public StockDTO selectStockBySeq(int stockSeq) {
        return sqlSession.selectOne(NAMESPACE + "selectStockBySeq", stockSeq);
    }

    public void updateCurrentStock(Map<String, Object> params) {
        sqlSession.update(NAMESPACE + "updateCurrentStock", params);
    }

    public void insertBatch(StockBatchDTO batch) {
        sqlSession.insert(NAMESPACE + "insertBatch", batch);
    }

    public List<StockBatchDTO> selectAvailableBatches(int stockSeq) {
        return sqlSession.selectList(NAMESPACE + "selectAvailableBatches", stockSeq);
    }

    public void updateBatchQuantity(StockBatchDTO batch) {
        sqlSession.update(NAMESPACE + "updateBatchQuantity", batch);
    }

    public void insertHistory(StockHistoryDTO history) {
        sqlSession.insert(NAMESPACE + "insertHistory", history);
    }

    public List<StockBatchDTO> selectBatchesByStockSeq(int stockSeq) {
        return sqlSession.selectList(NAMESPACE + "selectBatchesByStockSeq", stockSeq);
    }

    public List<StockHistoryDTO> selectHistoriesByStockSeq(int stockSeq) {
        return sqlSession.selectList(NAMESPACE + "selectHistoriesByStockSeq", stockSeq);
    }

    public void updateStock(StockDTO stock) {
        sqlSession.update(NAMESPACE + "updateStock", stock);
    }

    public void deleteStock(int stockSeq) {
        sqlSession.delete(NAMESPACE + "deleteStock", stockSeq);
    }

    public void deleteBatchesByStockSeq(int stockSeq) {
        sqlSession.delete(NAMESPACE + "deleteBatchesByStockSeq", stockSeq);
    }

    public void deleteHistoryByStockSeq(int stockSeq) {
        sqlSession.delete(NAMESPACE + "deleteHistoryByStockSeq", stockSeq);
    }

    public int selectgetcountExpiringSoon() {
        return sqlSession.selectOne(NAMESPACE + "selectgetcountExpiringSoon");
    }
}
