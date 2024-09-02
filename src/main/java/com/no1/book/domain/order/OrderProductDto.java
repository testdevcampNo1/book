package com.no1.book.domain.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderProductDto {

    private int ordProdId;
    private String ordId;
    private String prodId;
    private String ordChkCode;
    private String codeType;
    private String isEbook;
    private String dawnDeliChk;
    private String prodName;
    private String img;
    private String prodPageLink;
    private int ordQty;
    private int prodBasePrice;
    private int totalProdPrice;
    private int discPrice;
    private int totalDiscPrice;
    private int totalPayPrice;
    private String cancelableDate;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;

    // regDate, upDate - 현재시간으로 추가
    // cancelableDate - regDate + 7일
    public OrderProductDto(int ordProdId, String ordId, String prodId, String ordProdStusCode, String codeType, String ebookChk, String name, int ordQty, String img, String prodPageLink, String isDawnDelivery, int totalProdPrice, int totalDiscPrice, int totalPayPrice, String regId, String upId) {
        this.ordProdId = ordProdId;
        this.ordId = ordId;
        this.prodId = prodId;
        this.ordChkCode = ordChkCode;
        this.codeType = codeType;
        this.isEbook = isEbook;
        this.dawnDeliChk = dawnDeliChk;
        this.prodName = prodName;
        this.img = img;
        this.prodPageLink = prodPageLink;
        this.ordQty = ordQty;
        this.prodBasePrice = prodBasePrice;
        this.totalProdPrice = totalProdPrice;
        this.discPrice = discPrice;
        this.totalDiscPrice = totalDiscPrice;
        this.totalPayPrice = totalPayPrice;
        this.regId = regId;
        this.upId = upId;
    }
}
