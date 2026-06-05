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

@Service
public class FileService {

    @Autowired
    private FileDAO fileDao;

    @Autowired
    private Storage storage;

    private final String bucketName = "study_jcr";

    public boolean deleteFromGcs(String sysName) {
        if (sysName == null || sysName.isEmpty()) return false;
        try {
            return storage.delete(bucketName, sysName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String updateFile(MultipartFile file, Integer userSeq, String category, String oldSysName) throws IOException {
        // 1. 기존 파일이 있으면 삭제
        if (oldSysName != null && !oldSysName.isEmpty()) {
            deleteFromGcs(oldSysName);
            fileDao.deleteFile(oldSysName);
        }

        // 2. 새 파일 업로드
        return uploadToGcsAndGetUrl(file, userSeq, category);
    }

    public String uploadToGcsAndGetUrl(MultipartFile file, Integer userSeq, String category) throws IOException {
        if (file == null || file.isEmpty()) {
            return "";
        }

        String oriname = file.getOriginalFilename();
        String ext = "";
        if (oriname != null && oriname.contains(".")) {
            ext = oriname.substring(oriname.lastIndexOf("."));
        }

        // a. 카테고리명을 소문자로 바꾼 뒤 가상 폴더 경로 생성, UUID 결합하여 고유한 sysName 생성
        String sysName = category.toLowerCase() + "/" + UUID.randomUUID().toString() + ext;

        // b. 파일 바이너리를 GCS 버킷에 실제 업로드
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, sysName)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());

        // c. 업로드 완료 후, 메타데이터 장부 작성하여 FileRepository를 통해 files 테이블에 인서트
        FileSaveDto fileSaveDto = new FileSaveDto();
        fileSaveDto.setUserSeq(userSeq);
        fileSaveDto.setBoardSeq(null);
        fileSaveDto.setFileCategory(category);
        fileSaveDto.setOriname(oriname);
        fileSaveDto.setSysname(sysName);
        fileSaveDto.setFileSize(file.getSize());
        fileSaveDto.setFileType(file.getContentType());

        fileDao.insertFile(fileSaveDto);

        // d. 최종적으로 외부 브라우저에서 접근 가능한 공공 URL 주소 문자열 리턴
        return "https://storage.googleapis.com/" + bucketName + "/" + sysName;
    }
}
