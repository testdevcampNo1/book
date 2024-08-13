package com.no1.book.common.validator;

import com.no1.book.common.exception.*;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;

import java.util.List;

public class OrderValidator {
    // 필수입력정보 검증시 공통으로 사용
    private void validateNotEmpty(String value, String errorMessage) {
        if(value == null || value.isEmpty()) {
            throw new MissingRequiredOrderInfoException(errorMessage);
        }
    }

    // 주문 검증
    // 에러메시지의 경우 enum으로 관리
    public void validateOrder(OrderFormDto orderInfo) {
        validateNotEmpty(orderInfo.getEmail(), OrderValidatorErrorMessage.MISSING_EMAIL.getMessage());
        validateNotEmpty(orderInfo.getName(), OrderValidatorErrorMessage.MISSING_NAME.getMessage());
        validateNotEmpty(orderInfo.getTelNum(), OrderValidatorErrorMessage.MISSING_TEL_NUM.getMessage());

        if(orderInfo.getCustId() == -1) { // 비회원
            validateNotEmpty(orderInfo.getPwd(), OrderValidatorErrorMessage.MISSING_PASSWORD.getMessage());
        }

        validateNotEmpty(orderInfo.getZipCode(), OrderValidatorErrorMessage.MISSING_ZIP_CODE.getMessage());
        validateNotEmpty(orderInfo.getMainAddress(), OrderValidatorErrorMessage.MISSING_MAIN_ADDRESS.getMessage());
        validateNotEmpty(orderInfo.getDetailAddress(), OrderValidatorErrorMessage.MISSING_DETAIL_ADDRESS.getMessage());

        // 금액 검증
        validateOrderPrice(orderInfo);

        // 주문상품검증
        validateOrderProduct(orderInfo.getProductList());

        // 결제 정보 검증
        validatePaymentMethod(orderInfo.getPaymentMethod());

        // 결제 성공 여부 검증
        validateRequestPayment();
    }

    // 금액 검증
    private void validateOrderPrice(OrderFormDto orderFormDto) {
        if(orderFormDto.getTotalProdBasePrice() < 0 || orderFormDto.getTotalDiscPrice() < 0 || orderFormDto.getTotalSalePrice() < 0 || orderFormDto.getDlvPrice() < 0) {
            throw new InvalidAmountException(OrderValidatorErrorMessage.NEGATIVE_AMOUNT.getMessage());
        }

        if(orderFormDto.getTotalProdBasePrice() < orderFormDto.getTotalSalePrice()) {
            throw new InvalidAmountException(OrderValidatorErrorMessage.SALE_PRICE_EXCEEDS_BASE_PRICE.getMessage());
        }

        if(orderFormDto.getTotalSalePrice() < orderFormDto.getTotalDiscPrice()) {
            throw new InvalidAmountException(OrderValidatorErrorMessage.DISCOUNT_PRICE_EXCEEDS_SALE_PRICE.getMessage());
        }
    }

    // 주문상품 검증
    private void validateOrderProduct(List<OrderProductDto> orderProductDtoList) {
        // 주문 상품 리스트 검증
        if (orderProductDtoList == null || orderProductDtoList.isEmpty()) {
            throw new ProductNotOrderableException(OrderValidatorErrorMessage.EMPTY_PRODUCT_LIST.getMessage());
        }

        // 각 주문 상품 검증
        for (OrderProductDto product : orderProductDtoList) {
            // 상품id 검증
            if (product.getProdId() == null || product.getProdId().isEmpty()) {
                throw new InvalidProductIdException(OrderValidatorErrorMessage.MISSING_PRODUCT_ID.getMessage());
            }

            // 상품 상태 검증
            // 상품 상태 DB에서 실시간으로 조회 필요
            if (product.getOrdProdStusCode() == null || product.getOrdProdStusCode().isEmpty() || !product.getOrdProdStusCode().equals("AVBL") || product.getCodeType() == null || product.getCodeType().isEmpty()) {
                throw new ProductNotOrderableException(OrderValidatorErrorMessage.INVALID_PRODUCT_STATUS.getMessage());
            }

            // 0개 이하인 주문 수량 검증
            if (product.getOrdQty() <= 0) {
                throw new ProductNotOrderableException(OrderValidatorErrorMessage.ZERO_OR_NEGATIVE_QUANTITY.getMessage());
            }

            // 재고 부족 검증
            // 재고 DB에서 실시간으로 조회 필요
//            if(getProductStockCount(product.getProdId()) < product.getOrdQty()) {
//                throw new ProductNotOrderableException("재고가 부족한 상품이 존재합니다.");
//            }

            // 상품 금액 검증
            if (product.getTotalProdPrice() < 0 || product.getTotalDiscPrice() < 0 || product.getTotalPayPrice() < 0) {
                throw new InvalidAmountException(OrderValidatorErrorMessage.NEGATIVE_AMOUNT.getMessage());
            }

            if (product.getTotalProdPrice() < product.getTotalPayPrice()) {
                throw new InvalidAmountException(OrderValidatorErrorMessage.SALE_PRICE_EXCEEDS_BASE_PRICE.getMessage());
            }

            if (product.getTotalPayPrice() < product.getTotalDiscPrice()) {
                throw new InvalidAmountException(OrderValidatorErrorMessage.DISCOUNT_PRICE_EXCEEDS_SALE_PRICE.getMessage());
            }
        }
    }

    // 결제 정보 검증
    private void validatePaymentMethod(String paymentMethod) {
        if(paymentMethod == null || paymentMethod.isEmpty()) {
            throw new MissingRequiredOrderInfoException(OrderValidatorErrorMessage.MISSING_PAYMENT_METHOD.getMessage());
        }
    }

    // 결제 성공 여부 검증
    private void validateRequestPayment() {
//        throw new PaymentFailureException(OrderValidatorErrorMessage.PAYMENT_FAILED.getMessage());
    }
}
