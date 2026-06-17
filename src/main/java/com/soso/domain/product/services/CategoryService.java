package com.soso.domain.product.services;

import com.soso.domain.product.dao.CategoryDAO;
import com.soso.domain.product.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public List<CategoryDTO> getAllCategories() {
        return categoryDAO.selectAllCategories();
    }
}
