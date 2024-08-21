package com.no1.book.controller.product;

import com.no1.book.domain.product.ReviewDto;
import com.no1.book.service.product.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add/{prodId}")
    public ResponseEntity<String> addReview(@PathVariable String prodId, @RequestBody ReviewDto reviewDto, HttpSession session) {
        String custId = (String) session.getAttribute("custId");
//        String custId = "CUST001"; // 임시 하드코딩

        if (custId == null) { // 세션에 id가 없으면 (로그인이 안되어있으면)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        reviewDto.setCustId(custId);
        reviewDto.setProdId(prodId);

        reviewService.addReview(reviewDto);
        return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Integer reviewId, @RequestBody ReviewDto reviewDto, HttpSession session) {
        String custId = (String) session.getAttribute("custId");
//        String custId = "CUST001"; // 임시 하드코딩

        if (custId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        reviewDto.setCustId(custId);
        reviewDto.setReviewId(reviewId);

        reviewService.updateReview(reviewDto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId, HttpSession session) {
        String custId = (String) session.getAttribute("custId");
//        String custId = "CUST001"; // 임시 하드코딩

        if (custId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        reviewService.deleleReviewById(reviewId);
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/list/{prodId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable String prodId) {
        List<ReviewDto> reviews = reviewService.reviewsPerProduct(prodId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Integer reviewId) {
        ReviewDto review = reviewService.findReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 처리 중 오류가 발생했습니다.");
    }

}
