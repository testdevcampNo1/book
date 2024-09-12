package com.no1.book.domain.order;

import java.util.*;

public class CartProdDto {
    String custId;
    String prodId;
    Integer itemQty;
    String imageId;
    String prodName;
    Integer prodBasePrice;
    Integer discRate;
    Integer discPrice;
    Integer salePrice;
    String ordAbStusCode;
    String registerDate;
    String expiredDate;
    String isEbook;
    String dawnDeliChk;


    public CartProdDto() {
    }

    public CartProdDto(String custId, String prodId, Integer salePrice, String registerDate) {
        this.custId = custId;
        this.prodId = prodId;
        this.salePrice = salePrice;
        this.registerDate = registerDate;
    }


    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getProdBasePrice() {
        return prodBasePrice;
    }

    public void setProdBasePrice(Integer prodBasePrice) {
        this.prodBasePrice = prodBasePrice;
    }

    public Integer getDiscRate() {
        return discRate;
    }

    public void setDiscRate(Integer discRate) {
        this.discRate = discRate;
    }

    public Integer getDiscPrice() {
        return discPrice;
    }

    public void setDiscPrice(Integer discPrice) {
        this.discPrice = discPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public String getOrdAbStusCode() {
        return ordAbStusCode;
    }

    public void setOrdAbStusCode(String ordAbStusCode) {
        this.ordAbStusCode = ordAbStusCode;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
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

    @Override
    public String toString() {
        return "CartProdDto{" +
                "custId=" + custId +
                ", prodId='" + prodId + '\'' +
                ", itemQty=" + itemQty +
                ", imageId='" + imageId + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodBasePrice=" + prodBasePrice +
                ", discRate=" + discRate +
                ", discPrice=" + discPrice +
                ", salePrice=" + salePrice +
                ", ordAbStusCode='" + ordAbStusCode + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", isEbook='" + isEbook + '\'' +
                ", dawnDeliChk='" + dawnDeliChk + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProdDto that = (CartProdDto) o;
        return Objects.equals(custId, that.custId) && Objects.equals(prodId, that.prodId) && Objects.equals(itemQty, that.itemQty) && Objects.equals(imageId, that.imageId) && Objects.equals(prodName, that.prodName) && Objects.equals(prodBasePrice, that.prodBasePrice) && Objects.equals(discRate, that.discRate) && Objects.equals(discPrice, that.discPrice) && Objects.equals(salePrice, that.salePrice) && Objects.equals(ordAbStusCode, that.ordAbStusCode) && Objects.equals(registerDate, that.registerDate) && Objects.equals(expiredDate, that.expiredDate) && Objects.equals(isEbook, that.isEbook) && Objects.equals(dawnDeliChk, that.dawnDeliChk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(custId, prodId, itemQty, imageId, prodName, prodBasePrice, discRate, discPrice, salePrice, ordAbStusCode, registerDate, expiredDate, isEbook, dawnDeliChk);
    }
}
