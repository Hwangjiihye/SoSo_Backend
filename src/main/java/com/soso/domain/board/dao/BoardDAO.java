package com.soso.domain.board.dao;

import com.soso.domain.board.dto.BoardInsertRequestDto;
import com.soso.domain.board.dto.BoardResponseDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @file BoardDAO.java
 * @description 게시판 관련 DB 접근을 담당하는 DAO 클래스입니다.
 */
@Repository
public class BoardDAO {

    @Autowired
    private SqlSessionTemplate mybatis;

    private static final String NAMESPACE = "com.soso.domain.board.dao.BoardDAO";

    /**
     * 특정 타입의 게시글 목록을 조회합니다 (예: NOTICE, TIP)
     */
    public List<BoardResponseDto> getBoardsByType(String boardType) {
        return mybatis.selectList(NAMESPACE + ".getBoardsByType", boardType);
    }

    /**
     * 특정 유저가 작성한 1:1 문의(CS) 목록을 조회합니다.
     */
    public List<BoardResponseDto> getMyInquiries(int userSeq) {
        return mybatis.selectList(NAMESPACE + ".getMyInquiries", userSeq);
    }

    /**
     * 새로운 문의(게시글)를 등록합니다.
     */
    public int insertInquiry(BoardInsertRequestDto dto) {
        return mybatis.insert(NAMESPACE + ".insertInquiry", dto);
    }
}