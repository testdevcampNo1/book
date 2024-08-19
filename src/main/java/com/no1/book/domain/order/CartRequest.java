package com.no1.book.domain.order;

import java.util.Objects;

public class CartRequest {
    Integer custId;
    String prodId;
    Integer itemQty;

    public CartRequest(){}
    public CartRequest(Integer custId, String prodId, Integer itemQty) {
        this.custId = custId;
        this.prodId = prodId;
        this.itemQty = itemQty;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
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

    @Override
    public String toString() {
        return "CartRequest{" +
                "custId=" + custId +
                ", prodId='" + prodId + '\'' +
                ", itemQty=" + itemQty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartRequest that = (CartRequest) o;
        return custId == that.custId && itemQty == that.itemQty && Objects.equals(prodId, that.prodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(custId, prodId, itemQty);
    }
}
