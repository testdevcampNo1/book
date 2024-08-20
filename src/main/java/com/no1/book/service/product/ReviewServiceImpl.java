package com.no1.book.service.product;

import com.no1.book.dao.product.ReviewDao;
import com.no1.book.domain.product.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewDao reviewDao;


    @Override
    public void addReview(ReviewDto reviewDto) {
        reviewDao.add(reviewDto);
    }

    @Override
    public int getReviewCount() {
        return reviewDao.count();
    }

    @Override
    public ReviewDto findReviewById(Integer reviewId) {
        return reviewDao.select(reviewId);
    }

    @Override
    public void deleleReviewById(Integer reviewId) {
        reviewDao.delete(reviewId);
    }

    @Override
    public void updateReview(ReviewDto reviewDto) {
        reviewDao.update(reviewDto);
    }

    @Override
    public List<ReviewDto> reviewsPerProduct(String prodId) {
        return reviewDao.reviewsPerProduct(prodId);
    }
}
