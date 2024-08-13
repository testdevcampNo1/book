package com.no1.book.common.exception;

// 주문에서 발생하는 예외의 최상위 클래스
// RuntimeException을 상속받는다.
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
