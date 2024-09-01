package com.no1.book.dao.product;

import com.no1.book.domain.product.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryDaoTest {

    @Autowired
    private CategoryDao categoryDao;

    @Test
    void getAllCategoriesTest() {
        List<CategoryDto> cateList = categoryDao.getAllCategories();

        for (int i = 0; i < cateList.size(); i++) {
            System.out.println(cateList.get(i).getCateCode() + " " + cateList.get(i).getCateName());
        }
    }

    @Test
    void getCateNowTest() {
        List<CategoryDto> cateList = categoryDao.getAllCategories();

        for (int i = 0; i < cateList.size(); i++) {
            System.out.println(cateList.get(i).getCateCode() + " " + cateList.get(i).getCateNow());
        }
    }

    @Test
    void getAllFinalCategoriesTest() {
        List<CategoryDto> finalCateList = categoryDao.getAllFinalCategories();

        for (int i = 0; i < finalCateList.size(); i++) {
            System.out.println(finalCateList.get(i).getCateCode() + " " + finalCateList.get(i).getCateName());
        }
    }



}