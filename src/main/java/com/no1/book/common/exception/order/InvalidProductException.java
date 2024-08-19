package com.no1.book.common.exception.order;

// 유효하지 않은 상품일 경우 던지는 예외
public class InvalidProductException extends OrderException {
    public InvalidProductException(String message) { super(message); }
    public InvalidProductException(String message, Throwable cause) { super(message, cause); }
}
