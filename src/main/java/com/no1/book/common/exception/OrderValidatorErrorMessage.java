package com.no1.book.common.exception;

public enum OrderValidatorErrorMessage {
    MISSING_EMAIL("이메일 정보가 필요합니다."),
    MISSING_NAME("수령인 이름 정보가 필요합니다."),
    MISSING_TEL_NUM("수령인 전화번호 정보가 필요합니다."),
    MISSING_PASSWORD("주문 조회 비밀번호가 필요합니다."),
    MISSING_ZIP_CODE("우편번호가 필요합니다."),
    MISSING_MAIN_ADDRESS("기본 주소가 필요합니다."),
    MISSING_DETAIL_ADDRESS("상세 주소가 필요합니다."),
    NEGATIVE_AMOUNT("음수인 금액이 존재합니다."),
    SALE_PRICE_EXCEEDS_BASE_PRICE("정가의 합보다 판매가의 합이 더 큽니다."),
    DISCOUNT_PRICE_EXCEEDS_SALE_PRICE("판매가의 합보다 할인가의 합이 더 큽니다."),
    MISSING_PAYMENT_METHOD("결제 수단이 존재하지 않습니다."),
    EMPTY_PRODUCT_LIST("주문 상품 목록이 존재하지 않습니다."),
    MISSING_PRODUCT_ID("주문 상품 id가 존재하지 않습니다."),
    INVALID_PRODUCT_STATUS("주문 불가능한 상품이 존재합니다."),
    ZERO_OR_NEGATIVE_QUANTITY("주문 수량이 0개 이하인 상품이 존재합니다."),
    INSUFFICIENT_STOCK("재고가 부족한 상품이 존재합니다."),
    SAVE_DATABASE_FAILED("DB에 저장 실패했습니다."),
    PAYMENT_FAILED("결제 실패했습니다.");

    private final String message;

    OrderValidatorErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
