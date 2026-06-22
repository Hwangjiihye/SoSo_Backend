package com.soso.domain.board.dto;

/**
 * @file BoardInsertRequestDto.java
 * @description 새로운 게시글(문의 등)을 등록할 때 사용하는 DTO입니다.
 */
public class BoardInsertRequestDto {
    private int userSeq;
    private String boardType; // 'CS', 'NOTICE', 'TIP' 등
    private String csType;    // 'PAY', 'SERVICE', 'ACCOUNT', 'BUG'
    private String title;
    private String content;

    public BoardInsertRequestDto() {}

    public BoardInsertRequestDto(int userSeq, String boardType, String csType, String title, String content) {
        this.userSeq = userSeq;
        this.boardType = boardType;
        this.csType = csType;
        this.title = title;
        this.content = content;
    }

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
}