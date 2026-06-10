package com.soso.domain.file.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.soso.domain.file.dao.FileDAO;
import com.soso.domain.file.dto.FileSaveDto;

/**
 * @file FileService.java
 * @description 파일 업로드 및 삭제를 담당하는 서비스입니다.
 * 구글 클라우드 스토리지(GCS)와 DB의 files 테이블을 함께 관리합니다.
 */
@Service
public class FileService {

    @Autowired
    private FileDAO fileDao; // DB에 파일 정보를 저장하기 위한 DAO

    @Autowired
    private Storage storage; // 구글 클라우드 스토리지 라이브러리

    // 파일을 저장할 구글 클라우드 스토리지의 바구니(Bucket) 이름
    private final String bucketName = "study_jcr";

    /**
     * 🗑️ 구글 클라우드 스토리지에서 파일 삭제
     * @param sysName - 저장된 시스템 파일명 (경로 포함)
     * @return 삭제 성공 여부
     */
    public boolean deleteFromGcs(String sysName) {
        if (sysName == null || sysName.isEmpty()) return false;
        try {
            // 해당 버킷에서 파일을 찾아 삭제합니다.
            return storage.delete(bucketName, sysName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 🔄 기존 파일을 새 파일로 교체
     * @param file - 새로 업로드할 파일
     * @param userSeq - 사용자 번호
     * @param category - 파일 카테고리 (예: STORE_IMAGE)
     * @param oldSysName - 삭제할 기존 파일의 시스템 파일명
     * @return 새 파일의 접근 URL
     */
    public String updateFile(MultipartFile file, Integer userSeq, String category, String oldSysName) throws IOException {
        // 1. 기존 파일이 있으면 GCS와 DB에서 모두 삭제합니다.
        if (oldSysName != null && !oldSysName.isEmpty()) {
            deleteFromGcs(oldSysName);
            fileDao.deleteFile(oldSysName);
        }

        // 2. 새 파일을 업로드합니다.
        return uploadToGcsAndGetUrl(file, userSeq, category);
    }

    /**
     * ⬆️ 파일 업로드 (기본형)
     * @param file - 업로드할 파일
     * @param userSeq - 사용자 번호
     * @param category - 파일 카테고리
     * @return 업로드된 파일의 접근 URL
     */
    public String uploadToGcsAndGetUrl(MultipartFile file, Integer userSeq, String category) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }

        // 1. 원본 파일명과 확장자를 추출합니다.
        String oriname = file.getOriginalFilename();
        String ext = "";
        if (oriname != null && oriname.contains(".")) {
            ext = oriname.substring(oriname.lastIndexOf("."));
        }

        // 2. 중복을 방지하기 위해 랜덤한 UUID를 사용하여 새로운 시스템 파일명을 만듭니다.
        // 예: store_image/a1b2c3d4-e5f6...png
        String sysName = category.toLowerCase() + "/" + UUID.randomUUID().toString() + ext;

        // 3. 구글 클라우드 스토리지(GCS)에 실제 파일을 업로드합니다.
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, sysName)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());

        // 4. DB(files 테이블)에 파일 메타데이터(원본명, 시스템명, 크기 등)를 저장합니다.
        FileSaveDto fileSaveDto = new FileSaveDto();
        fileSaveDto.setUserSeq(userSeq);
        fileSaveDto.setBoardSeq(null); // 특정 게시글 번호가 없는 경우
        fileSaveDto.setFileCategory(category);
        fileSaveDto.setOriname(oriname);
        fileSaveDto.setSysname(sysName);
        fileSaveDto.setFileSize(file.getSize());
        fileSaveDto.setFileType(file.getContentType());

        fileDao.insertFile(fileSaveDto);

        // 5. 브라우저에서 접근 가능한 URL 주소를 반환합니다.
        return "https://storage.googleapis.com/" + bucketName + "/" + sysName;
    }

    /**
     * ⬆️ 파일 업로드 (특정 게시글/매장 번호 포함)
     * @param file - 업로드할 파일
     * @param userSeq - 사용자 번호
     * @param category - 파일 카테고리
     * @param boardSeq - 연결할 게시글 또는 매장의 고유 번호
     * @return 업로드된 파일의 접근 URL
     */
    public String uploadToGcsAndGetUrlWithBoardSeq(MultipartFile file, Integer userSeq, String category, Integer boardSeq) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }

        String oriname = file.getOriginalFilename();
        String ext = "";
        if (oriname != null && oriname.contains(".")) {
            ext = oriname.substring(oriname.lastIndexOf("."));
        }

        // 시스템 파일명 생성 (랜덤 UUID 사용)
        String sysName = category.toLowerCase() + "/" + UUID.randomUUID().toString() + ext;

        // GCS 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, sysName)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());

        // DB 저장 (이번에는 boardSeq를 포함하여 어떤 데이터에 속한 파일인지 기록합니다)
        FileSaveDto fileSaveDto = new FileSaveDto();
        fileSaveDto.setUserSeq(userSeq);
        fileSaveDto.setBoardSeq(boardSeq); // 매장 번호 등을 여기에 저장!
        fileSaveDto.setFileCategory(category);
        fileSaveDto.setOriname(oriname);
        fileSaveDto.setSysname(sysName);
        fileSaveDto.setFileSize(file.getSize());
        fileSaveDto.setFileType(file.getContentType());

        fileDao.insertFile(fileSaveDto);

        return "https://storage.googleapis.com/" + bucketName + "/" + sysName;
    }
}
