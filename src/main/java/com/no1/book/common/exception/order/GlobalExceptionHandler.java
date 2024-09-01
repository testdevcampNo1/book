package com.no1.book.common.exception.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 모든 컨트롤러에서 발생하는 예외를 중앙에서 처리하는 클래스
@ControllerAdvice(basePackages = "com.no1.book.controller.order")
public class GlobalExceptionHandler {

    // Database 작업 실패시 던지는 예외
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<String> handleDatabaseException(SystemException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않은 회원일 경우 던지는 예외
    @ExceptionHandler(InvalidCustomerException.class)
    public ResponseEntity<String> handleCustomerException(InvalidCustomerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유효하지 않은 주문일 경우 던지는 예외
    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<String> handleOrderException(InvalidOrderException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유효하지 않은 상품일 경우 던지는 예외
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<String> handleProductException(InvalidProductException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유효하지 않은 결제일 경우 던지는 예외
    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<String> handlePaymentException(InvalidPaymentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 주문 실패 처리 - 주문에서 발생하는 모든 예외의 최상위
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrderException(OrderException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
