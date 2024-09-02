package com.no1.book.service.product;

import com.no1.book.dao.product.ProductDao;
import com.no1.book.dao.product.ReviewDao;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.domain.product.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private ProductDao productDao;


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
    public void deleteReviewById(Integer reviewId) {
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

    // 해당 상품에 리뷰가 없으면 어떻게 처리할지 생각해야함
    @Override
    public void calculateAvgStar(ProductDto dto) throws Exception {
        String prodId = dto.getProdId();

        // 상품 id에 해당하는 리뷰 리스트를 가져와 각 리뷰의 별점을 더하기
        List<ReviewDto> reviewList = reviewDao.reviewsPerProduct(prodId);
        float totalStars = (float)reviewList.stream().mapToDouble(ReviewDto::getStarPt).sum();
        // 해당 상품에 대한 평균 별점 계산
        float avgStar = Math.round((totalStars / (float) reviewList.size()) * 100) / 100.0f;

        // 계산된 별점 업데이트
        ProductDto prod = productDao.select(prodId);
        prod.setStarAvg(avgStar);
        productDao.update(prod);
        // 근데 이 메서드 언제 호출해야 하지? .....
    }


}
