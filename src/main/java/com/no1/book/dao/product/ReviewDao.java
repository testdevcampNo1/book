package com.no1.book.dao.product;

import com.no1.book.domain.product.ReviewDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReviewDao {

    int count();

    void deleteAll();

    ReviewDto select(int reviewId);

    void add(ReviewDto reviewDto);

    void delete(int reviewID);

    void update(ReviewDto reviewDto);

    List<ReviewDto> reviewsPerProduct(String prodId);
}
