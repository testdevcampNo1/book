package com.no1.book.common.exception.order;

// 유효하지 않은 회원일 경우 던지는 예외
public class InvalidCustomerException extends OrderException {
    public InvalidCustomerException(String message) { super(message); }
    public InvalidCustomerException(String message, Throwable cause) { super(message, cause); }
}