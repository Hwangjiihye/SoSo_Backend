package com.soso.domain.file.dto;

public class FileSaveDto {
    private Integer userSeq;
    private Integer boardSeq;
    private String fileCategory;
    private String oriname;
    private String sysname;
    private Long fileSize;
    private String fileType;

    public FileSaveDto() {}

    public FileSaveDto(Integer userSeq, Integer boardSeq, String fileCategory, String oriname, String sysname, Long fileSize, String fileType) {
        this.userSeq = userSeq;
        this.boardSeq = boardSeq;
        this.fileCategory = fileCategory;
        this.oriname = oriname;
        this.sysname = sysname;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public Integer getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Integer userSeq) {
        this.userSeq = userSeq;
    }

    public Integer getBoardSeq() {
        return boardSeq;
    }

    public void setBoardSeq(Integer boardSeq) {
        this.boardSeq = boardSeq;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getOriname() {
        return oriname;
    }

    public void setOriname(String oriname) {
        this.oriname = oriname;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
