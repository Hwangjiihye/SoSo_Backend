package com.soso.domain.board.dto;

import java.time.LocalDateTime;

/**
 * @file BoardResponseDto.java
 * @description 게시판 (boards) 목록 조회를 위한 DTO입니다.
 */
public class BoardResponseDto {
    private int boardSeq;
    private int userSeq;
    private String boardType;
    private String csType;
    private String title;
    private String content;
    private int views;
    private LocalDateTime createdAt;
    
    // 조인을 통해 가져올 작성자 정보 (옵션)
    private String authorName;

    public BoardResponseDto() {}

    public BoardResponseDto(int boardSeq, int userSeq, String boardType, String csType, String title, String content, int views, LocalDateTime createdAt, String authorName) {
        this.boardSeq = boardSeq;
        this.userSeq = userSeq;
        this.boardType = boardType;
        this.csType = csType;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createdAt = createdAt;
        this.authorName = authorName;
    }

    public int getBoardSeq() { return boardSeq; }
    public void setBoardSeq(int boardSeq) { this.boardSeq = boardSeq; }
    
    public int getUserSeq() { return userSeq; }
    public void setUserSeq(int userSeq) { this.userSeq = userSeq; }
    
    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
    
    public String getCsType() { return csType; }
    public void setCsType(String csType) { this.csType = csType; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}