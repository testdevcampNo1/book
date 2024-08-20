package com.no1.book.dao.product;

import com.no1.book.domain.product.ReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewDaoTest {

    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private ProductDao productDao;

    @Test
    void countAndDeleteAllTest() {
        reviewDao.count();

        int cnt = reviewDao.count();

//        assertEquals(0, cnt);
    }

    @Test
    void selectAndAddTest() {
        reviewDao.deleteAll();

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewId(999)
                .prodId("PROD001")
                .custId("CUST001")
                .content("임시 내용")
                .starPt(8)
                .build();


        reviewDao.add(reviewDto);


        // auto_increment라 테스트 진행할 때 마다 review_id가 바뀌는데 검증을 어떻게 하는거지?
//        ReviewDto selectedReview = reviewDao.select(reviewDto.getReviewId());
//        System.out.println(selectedReview.getReviewId() + selectedReview.getContent());
//        assertEquals(selectedReview.getStarPt(), reviewDto.getStarPt());

    }

//    @Test
//    void deleteTest() {
//        reviewDao.delete(5);
//
//    }

//    @Test
//    void updateTest() {
//        // 1단계 데이터 선택 -> 임의의 dto
//        ReviewDto beforeUpdateDto = ReviewDto.builder()
//                .prodId("PROD001")
//                .custId("CUST001")
//                .content("수정 전 내용")
//                .starPt(9)
//                .rcmdCount(0)
//                .regDate("2023-08-07")
//                .regId("IMSIID")
//                .build();
//
//        // 2단계 데이터 처리 -> 업데이트
//        ReviewDto afterUpdateDto = ReviewDto.builder()
//                .reviewId(14)
//                .content("수정 후 내용")
//                .starPt(10)
//                .build();
//        reviewDao.update(afterUpdateDto);
//    }




}