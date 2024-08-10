package com.no1.book.dao.product;

import com.no1.book.domain.product.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao {
    List<CategoryDto> getAllCategories();
}