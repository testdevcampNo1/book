package com.no1.book.controller.product;

import com.no1.book.domain.product.ReviewDto;
import com.no1.book.service.product.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody ReviewDto reviewDto, HttpSession session) {
//        String custId = (String) session.getAttribute("custId");
        String custId = "CUST001";
        reviewDto.setCustId(custId);

        reviewService.addReview(reviewDto);
        return ResponseEntity.ok("리뷰가 성공적으로 작성되었습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateReview(@RequestBody ReviewDto reviewDto, HttpSession session) {
//        String custId = (String) session.getAttribute("custId");
        String custId = "CUST001";
        reviewDto.setCustId(custId);

        reviewService.updateReview(reviewDto);
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(@RequestParam("reviewId") Integer reviewId, HttpSession session) {

        reviewService.deleleReviewById(reviewId);
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@RequestParam("prodId") String prodId) {
        List<ReviewDto> reviews = reviewService.reviewsPerProduct(prodId);
        return ResponseEntity.ok(reviews);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 처리 중 오류가 발생했습니다.");
    }

}
