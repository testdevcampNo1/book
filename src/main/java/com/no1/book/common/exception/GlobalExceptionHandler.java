package com.no1.book.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 모든 컨트롤러에서 발생하는 예외를 중앙에서 처리하는 클래스
@ControllerAdvice
public class GlobalExceptionHandler {

    // 상품 재고 부족 처리
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStockException(OutOfStockException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 키 중복 처리
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 상품 ID 유효하지 않거나 존재하지 않을 경우 처리
    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleInvalidProductIdException(InvalidProductIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 회원 정보 DB 조회 실패 처리
    @ExceptionHandler(NotFoundCustomerException.class)
    public ResponseEntity<String> handleNotFoundCustomerException(NotFoundCustomerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 결제 실패 처리
    @ExceptionHandler(PaymentFailureException.class)
    public ResponseEntity<String> handlePaymentFailureException(PaymentFailureException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.PAYMENT_REQUIRED);
    }

    // 주문 상품 주문 불가 상태 처리
    @ExceptionHandler(ProductNotOrderableException.class)
    public ResponseEntity<String> handleProductNotOrderableException(ProductNotOrderableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // DB 연결 오류 처리
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<String> handleSystemException(SystemException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 주문 필수 입력 정보 누락
    @ExceptionHandler(MissingRequiredOrderInfoException.class)
    public ResponseEntity<String> handleMissingRequiredOrderInfoException(MissingRequiredOrderInfoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmountException(InvalidAmountException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 주문 실패 처리 - 주문에서 발생하는 모든 예외의 최상위
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrderException(OrderException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
