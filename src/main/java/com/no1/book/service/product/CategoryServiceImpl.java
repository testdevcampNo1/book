package com.no1.book.service.product;

import com.no1.book.dao.product.CategoryDao;
import com.no1.book.domain.product.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<CategoryDto> getAllCategories() throws Exception {
        return categoryDao.getAllCategories();
    }

    @Override
    public List<CategoryDto> getAllFinalCategories() throws Exception {
        return categoryDao.getAllFinalCategories();
    }
}
