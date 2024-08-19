package com.no1.book.common.exception.order;

// 유효하지 않은 주문일 경우 던지는 예외
public class InvalidOrderException extends OrderException {
    public InvalidOrderException(String message) { super(message); }
    public InvalidOrderException(String message, Throwable cause) { super(message, cause); }
}