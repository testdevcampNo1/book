package com.no1.book.common.exception.order;

// 결제 실패시 던지는 예외
public class PaymentFailureException extends OrderException {
    public PaymentFailureException(String message) {
        super(message);
    }

    public PaymentFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
