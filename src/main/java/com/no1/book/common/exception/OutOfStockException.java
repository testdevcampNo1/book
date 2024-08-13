package com.no1.book.common.exception;

// 재고 부족시 던지는 예외
public class OutOfStockException extends OrderException {
    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
