package com.no1.book.common.exception.order;

// 유효하지 않은 상품Id일 때 던지는 예외
public class InvalidProductIdException extends OrderException {
    public InvalidProductIdException(String message) {
        super(message);
    }

    public InvalidProductIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
