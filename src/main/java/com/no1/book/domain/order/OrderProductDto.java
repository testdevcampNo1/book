package com.no1.book.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderProductDto {
    private int ordProdId;
    private int ordId;
    private String prodId;
    private String ordProdStusCode;
    private String codeType;
    private String ebookChk;
    private String isDawnDelivery;
    private String name;
    private int ordQty;
    private String img;
    private String prodPageLink;
    private int totalProdPrice;
    private int totalDiscPrice;
    private int totalPayPrice;
    private String cancelableDate;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;

    // regDate, upDate - 현재시간으로 추가
    // cancelableDate - regDate + 7일
    @Builder
    public OrderProductDto(int ordProdId, int ordId, String prodId, String ordProdStusCode, String codeType, String ebookChk, String name, int ordQty, String img, String prodPageLink, String isDawnDelivery, int totalProdPrice, int totalDiscPrice, int totalPayPrice, String regId, String upId) {
        this.ordProdId = ordProdId;
        this.ordId = ordId;
        this.prodId = prodId;
        this.ordProdStusCode = ordProdStusCode;
        this.codeType = codeType;
        this.ebookChk = ebookChk;
        this.name = name;
        this.ordQty = ordQty;
        this.img = img;
        this.prodPageLink = prodPageLink;
        this.isDawnDelivery = isDawnDelivery;
        this.totalProdPrice = totalProdPrice;
        this.totalDiscPrice = totalDiscPrice;
        this.totalPayPrice = totalPayPrice;
        this.regId = regId;
        this.upId = upId;
    }

    public OrderProductDto(int ordProdId, int ordId, String prodId, String ordProdStusCode, String codeType, String ebookChk, String isDawnDelivery, String name, int ordQty, String img, String prodPageLink, int totalProdPrice, int totalDiscPrice, int totalPayPrice, String cancelableDate, String regDate, String regId, String upDate, String upId) {
        this.ordProdId = ordProdId;
        this.ordId = ordId;
        this.prodId = prodId;
        this.ordProdStusCode = ordProdStusCode;
        this.codeType = codeType;
        this.ebookChk = ebookChk;
        this.isDawnDelivery = isDawnDelivery;
        this.name = name;
        this.ordQty = ordQty;
        this.img = img;
        this.prodPageLink = prodPageLink;
        this.totalProdPrice = totalProdPrice;
        this.totalDiscPrice = totalDiscPrice;
        this.totalPayPrice = totalPayPrice;
        this.cancelableDate = cancelableDate;
        this.regDate = regDate;
        this.regId = regId;
        this.upDate = upDate;
        this.upId = upId;
    }

    public int getOrdProdId() {
        return ordProdId;
    }

    public int getOrdId() {
        return ordId;
    }

    public String getProdId() {
        return prodId;
    }

    public String getOrdProdStusCode() {
        return ordProdStusCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public String getEbookChk() {
        return ebookChk;
    }

    public String getName() {
        return name;
    }

    public int getOrdQty() {
        return ordQty;
    }

    public String getImg() {
        return img;
    }

    public String getProdPageLink() {
        return prodPageLink;
    }

    public int getTotalProdPrice() {
        return totalProdPrice;
    }

    public int getTotalDiscPrice() {
        return totalDiscPrice;
    }

    public int getTotalPayPrice() {
        return totalPayPrice;
    }

    public String getCancelableDate() {
        return cancelableDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getRegId() {
        return regId;
    }

    public String getUpDate() {
        return upDate;
    }

    public String getUpId() {
        return upId;
    }

    public String getIsDawnDelivery() { return isDawnDelivery; }
}
