package com.soso.domain.file.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soso.domain.file.dto.FileSaveDto;

@Repository
public class FileDAO {
	@Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.domain.file.repository.FileRepository";

    public int insertFile(FileSaveDto fileSaveDto) {
        return sqlSession.insert(NAMESPACE + ".insertFile", fileSaveDto);
    }
}
