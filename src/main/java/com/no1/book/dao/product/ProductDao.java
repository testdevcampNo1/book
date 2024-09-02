package com.no1.book.dao.product;

import com.no1.book.domain.product.CategoryDto;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.domain.product.SearchCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface ProductDao {

    int count() throws Exception;

    int delete(String prod_id) throws Exception;

    int insert(ProductDto dto) throws Exception;

    int deleteAll() throws Exception;

    ProductDto select(String prodId) throws Exception;

    int update(ProductDto dto) throws Exception;

    List<ProductDto> getFilteredAndSortedPage(Map<String, Object> map) throws Exception;

    List<CategoryDto> getAllCategories();

    int getFilteredAndSortedTotalSize(Map map) throws Exception;

    String getCateName(String prodId) throws Exception;

    int idChk(String prodId) throws Exception;

    String createId() throws  Exception;

    int plusSales (String prodId) throws Exception;

    List<ProductDto> getPage(SearchCondition sc) throws Exception;

    int getPageSize(SearchCondition sc) throws Exception;
}