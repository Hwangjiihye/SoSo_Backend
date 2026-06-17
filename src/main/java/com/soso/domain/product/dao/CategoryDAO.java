package com.soso.domain.product.dao;

import com.soso.domain.product.dto.CategoryDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String NAMESPACE = "com.soso.mappers.product.CategoryMapper.";

    public List<CategoryDTO> selectAllCategories() {
        return sqlSession.selectList(NAMESPACE + "selectAllCategories");
    }
}
