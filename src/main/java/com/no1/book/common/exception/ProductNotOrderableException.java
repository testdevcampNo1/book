package com.no1.book.common.exception;

// 상품이 주문 불가능한 상태인 경우 던지는 예외
public class ProductNotOrderableException extends OrderException {
    public ProductNotOrderableException(String message) {
        super(message);
    }

    public ProductNotOrderableException(String message, Throwable cause) {
        super(message, cause);
    }
}
