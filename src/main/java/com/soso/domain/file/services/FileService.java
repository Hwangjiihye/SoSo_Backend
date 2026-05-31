package com.soso.domain.file.services;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.soso.domain.file.dto.FileSaveDto;
import com.soso.domain.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private Storage storage;

    private final String bucketName = "study_jcr";

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

        fileRepository.insertFile(fileSaveDto);

        // d. 최종적으로 외부 브라우저에서 접근 가능한 공공 URL 주소 문자열 리턴
        return "https://storage.googleapis.com/" + bucketName + "/" + sysName;
    }
}
