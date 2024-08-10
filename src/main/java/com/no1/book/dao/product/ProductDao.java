package com.no1.book.dao.product;

import com.no1.book.domain.product.ProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductDao {
    int count() throws Exception;

    int deleteAll();

    int delete(String prod_id) throws Exception;

    int insert(ProductDto dto) throws Exception;

    List<ProductDto> sortByRegDateAsc(Map map) throws Exception;

    List<ProductDto> sortByRegDateDesc(Map map) throws Exception;

    List<ProductDto> sortByPriceAsc(Map map) throws Exception;

    List<ProductDto> sortByPriceDesc(Map map) throws Exception;

    List<ProductDto> sortByTotalSalesAsc(Map map) throws Exception;

    List<ProductDto> sortByTotalSalesDesc(Map map) throws Exception;

    List<ProductDto> filterByCategory(String cateKey) throws Exception;

    ProductDto select(String prodId) throws Exception;

    List<ProductDto> selectPage(Map map) throws Exception;

    int update(ProductDto dto) throws Exception;

}
