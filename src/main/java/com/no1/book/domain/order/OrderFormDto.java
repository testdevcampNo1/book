package com.no1.book.domain.order;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderFormDto {

    private String custId;
    // 노출에 필요한 상품 정보
    private List<OrderProductDto> productList;
    private String ordId;
    private String name;
    private String orderRequestMessage;
    private String pwd; // 비회원 주문 조회 비밀번호
    private String isAllEbook;
    private String isAllDawnDelivery;
    private String dlvDate;
    private String defaultChk;
    private int totalProdBasePrice;
    private int totalDiscPrice;
    private int totalPayPrice;
    private int dlvPrice;
    private int totalOrdQty;
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

    @Builder
    public OrderFormDto(String custId, List<OrderProductDto> productList, String ordId, String name, String orderRequestMessage, String pwd, String isAllEbook, String isAllDawnDelivery, String dlvDate, String defaultChk, int totalProdBasePrice, int totalDiscPrice, int totalPayPrice, int dlvPrice, int totalOrdQty, String email, String addressName, String telNum, String zipCode, String mainAddress, String detailAddress, String commonEntrancePassword, String paymentMethod) {
        this.custId = custId;
        this.productList = productList;
        this.ordId = ordId;
        this.name = name;
        this.orderRequestMessage = orderRequestMessage;
        this.pwd = pwd;
        this.isAllEbook = isAllEbook;
        this.isAllDawnDelivery = isAllDawnDelivery;
        this.dlvDate = dlvDate;
        this.defaultChk = defaultChk;
        this.totalProdBasePrice = totalProdBasePrice;
        this.totalDiscPrice = totalDiscPrice;
        this.totalPayPrice = totalPayPrice;
        this.dlvPrice = dlvPrice;
        this.totalOrdQty = totalOrdQty;
        this.email = email;
        this.addressName = addressName;
        this.telNum = telNum;
        this.zipCode = zipCode;
        this.mainAddress = mainAddress;
        this.detailAddress = detailAddress;
        this.commonEntrancePassword = commonEntrancePassword;
        this.paymentMethod = paymentMethod;
    }
}
