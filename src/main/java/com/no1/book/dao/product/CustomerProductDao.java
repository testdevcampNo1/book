package com.no1.book.dao.product;

import com.no1.book.domain.product.CustomerProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerProductDao {

    int insertCustomerProduct(CustomerProductDto customerProductDto);

    CustomerProductDto selectCustomerProduct(@Param("custId") String custId, @Param("prodId") String prodId);

    void updateReviewCnt(CustomerProductDto customerProductDto);

    void deleteCustomerProduct(@Param("custId") String custId, @Param("prodId") String prodId);

    int plusReviewCnt(CustomerProductDto customerProductDto);

    int minusReviewCnt(CustomerProductDto customerProductDto);

//    boolean existsCustomerProduct(CustomerProductDto customerProductDto);

}