package com.no1.book.common.exception.order;

// 유효하지 않은 결제일 경우 던지는 예외
public class InvalidPaymentException extends OrderException {
    public InvalidPaymentException(String message) { super(message); }
    public InvalidPaymentException(String message, Throwable cause) { super(message, cause); }
}
