package com.no1.book.controller.product;

import com.no1.book.domain.product.CustomerProductDto;
import com.no1.book.domain.product.ReviewDto;
import com.no1.book.service.product.ProductService;
import com.no1.book.service.product.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;

    @PostMapping("/add/{prodId}")
    public ResponseEntity<String> addReview(@PathVariable String prodId, @RequestBody ReviewDto reviewDto, HttpSession session) throws Exception {
        String custId = (String) session.getAttribute("custId");
//        String custId = "CUST001"; // 임시 하드코딩

        if (custId == null) { // 세션에 id가 없으면 (로그인이 안되어있으면)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        reviewDto.setCustId(custId);
        reviewDto.setProdId(prodId);

        CustomerProductDto CPdto = productService.getCustomerProduct(custId, prodId);

        if (CPdto == null) { // 구매한 적이 없는 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("상품을 구매한 고객만 리뷰를 등록할 수 있습니다.");
        }

        if (CPdto.getReviewCnt() > 0) { // 이미 리뷰를 작성한 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리뷰는 한 번만 등록할 수 있습니다.");
        }

        // 리뷰 작성 및 리뷰 카운트 증가
        reviewService.addReview(reviewDto);
        productService.plusReviewCnt(CPdto);

        try {
            reviewService.calculateAvgStar(productService.select(reviewDto.getProdId()));
        } catch (Exception e) { // 여기도 나중에 예외 정리하고 수정하기
            throw new RuntimeException(e);
        }
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
        try {
            reviewService.calculateAvgStar(productService.select(reviewDto.getProdId()));
        } catch (Exception e) { // 여기 예외 나중에 다시 생각하기 어떤 예외가 들어갈 수 있고 어떻게 처리하는지 등
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId, HttpSession session) throws Exception {
        String custId = (String) session.getAttribute("custId");
//        String custId = "CUST001"; // 임시 하드코딩

        if (custId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        ReviewDto reviewDto = reviewService.findReviewById(reviewId);

        // 리뷰 카운트 감소하고 리뷰 삭제
        CustomerProductDto CPdto = productService.getCustomerProduct(custId, reviewDto.getProdId());
        if (CPdto != null && CPdto.getReviewCnt() > 0) {
            productService.minusReviewCnt(CPdto);
        }
        reviewService.deleteReviewById(reviewId);

        try {
            reviewService.calculateAvgStar(productService.select(reviewDto.getProdId()));
        } catch (Exception e) { // 여기도
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/list/{prodId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable String prodId) {
        List<ReviewDto> reviews = reviewService.reviewsPerProduct(prodId);

        try {
            reviewService.calculateAvgStar(productService.select(prodId));
        } catch (Exception e) { // 여기도
            throw new RuntimeException(e);
        }
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
