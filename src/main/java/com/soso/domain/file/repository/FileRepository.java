package com.soso.domain.file.repository;

import com.soso.domain.file.dto.FileSaveDto;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.domain.file.repository.FileRepository";

    public int insertFile(FileSaveDto fileSaveDto) {
        return sqlSession.insert(NAMESPACE + ".insertFile", fileSaveDto);
    }
}
