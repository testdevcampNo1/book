package com.no1.book.common.exception;

// DB에서 회원 정보 조회에 실패할 때 던지는 예외
public class NotFoundCustomerException extends OrderException{
    public NotFoundCustomerException(String message) {
        super(message);
    }

    public NotFoundCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
