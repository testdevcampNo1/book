package com.no1.book.domain.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
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

    @Builder
    public OrderProductDto(int ordProdId, String ordId, String prodId, String ordChkCode, String codeType, String isEbook, String dawnDeliChk, String prodName, String img, String prodPageLink, int ordQty, int prodBasePrice, int totalProdPrice, int discPrice, int totalDiscPrice, int totalPayPrice, String regId, String upId) {
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

    public int getOrdProdId() {
        return ordProdId;
    }

    public void setOrdProdId(int ordProdId) {
        this.ordProdId = ordProdId;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getOrdChkCode() {
        return ordChkCode;
    }

    public void setOrdChkCode(String ordChkCode) {
        this.ordChkCode = ordChkCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getIsEbook() {
        return isEbook;
    }

    public void setIsEbook(String isEbook) {
        this.isEbook = isEbook;
    }

    public String getDawnDeliChk() {
        return dawnDeliChk;
    }

    public void setDawnDeliChk(String dawnDeliChk) {
        this.dawnDeliChk = dawnDeliChk;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProdPageLink() {
        return prodPageLink;
    }

    public void setProdPageLink(String prodPageLink) {
        this.prodPageLink = prodPageLink;
    }

    public int getOrdQty() {
        return ordQty;
    }

    public void setOrdQty(int ordQty) {
        this.ordQty = ordQty;
    }

    public int getProdBasePrice() {
        return prodBasePrice;
    }

    public void setProdBasePrice(int prodBasePrice) {
        this.prodBasePrice = prodBasePrice;
    }

    public int getDiscPrice() {
        return discPrice;
    }

    public void setDiscPrice(int discPrice) {
        this.discPrice = discPrice;
    }

    public int getTotalPayPrice() {
        return totalPayPrice;
    }

    public void setTotalPayPrice(int totalPayPrice) {
        this.totalPayPrice = totalPayPrice;
    }

    public String getCancelableDate() {
        return cancelableDate;
    }

    public void setCancelableDate(String cancelableDate) {
        this.cancelableDate = cancelableDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public String getUpId() {
        return upId;
    }

    public void setUpId(String upId) {
        this.upId = upId;
    }
}
