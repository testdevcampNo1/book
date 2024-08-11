package com.no1.book.domain.order;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class OrderFormDto {
    private int custId = -1; // 비회원 -1
    // 노출에 필요한 상품 정보
    private List<OrderProductDto> productList;
    private int totalProdBasePrice;
    private int totalDiscPrice;
    private int totalSalePrice;
    private int dlvPrice;
    private String orderRequestMessage;
    private String pwd; // 비회원 주문 조회 비밀번호
    private String isAllEbook;
    private String isAllDawnDelivery;
    private String dlvDate;
    private String defaultChk;
    private String name;
    // 배송 테이블에 저장할 정보
    private String email;
    private String addressName;
    private String telNum;
    private String zipCode;
    private String mainAddress;
    private String detailAddress;
    private String commonEntrancePassword; // 공동 현관 비밀번호
    // 결제시 필요한 정보
    private String paymentMethod;
}
