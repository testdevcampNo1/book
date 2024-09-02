package com.no1.book.domain.order;

import java.util.List;
import java.util.Objects;

public class CartDto {
    String custId;
    Integer itemQty;
    String prodId;
    String registerDate;
    String expiredDate;

    public CartDto() {}

    public CartDto(String custId, String prodId, Integer itemQty) {
        this.custId  = custId;
        this.prodId  = prodId;
        this.itemQty = itemQty;

    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
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



    @Override
    public String toString() {
        return "CartDto{" +
                "custId=" + custId +
                ", itemQty=" + itemQty +
                ", prodId='" + prodId + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return custId == cartDto.custId && itemQty == cartDto.itemQty && Objects.equals(prodId, cartDto.prodId) && Objects.equals(registerDate, cartDto.registerDate) && Objects.equals(expiredDate, cartDto.expiredDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(custId, itemQty, prodId, registerDate, expiredDate);
    }
}
