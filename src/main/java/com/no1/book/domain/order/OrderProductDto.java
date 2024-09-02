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
}
