package com.no1.book.service.product;

import com.no1.book.domain.product.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories() throws Exception;
}
