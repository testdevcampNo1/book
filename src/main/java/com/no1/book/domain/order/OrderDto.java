package com.no1.book.domain.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderDto {
    private int ordId;
    private int custId;
    private String ordStusCode;
    private String codeType;
    private String custChk; // 회원 여부
    private String ordDate; // 주문 일시
    private String ordReqMsg; // 주문 메시지
    private int totalProdPrice; // 총 상품 금액
    private int totalDiscPrice; // 총 할인 금액
    private int dlvPrice; // 배송비
    private int totalPayPrice; // 총 결제 금액
    private String pwd; // 비회원 주문조회 비밀번호
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;

    // TEST용
    // regDate, upDate, ordDate - 현재시간으로 추가
    public OrderDto(int ordId, int custId, String ordStusCode, String codeType, String custChk, String ordReqMsg, int totalProdPrice, int totalDiscPrice, int dlvPrice, int totalPayPrice, String pwd, String regId, String upId) {
        this.ordId = ordId;
        this.custId = custId;
        this.ordStusCode = ordStusCode;
        this.codeType = codeType;
        this.custChk = custChk;
        this.ordReqMsg = ordReqMsg;
        this.totalProdPrice = totalProdPrice;
        this.totalDiscPrice = totalDiscPrice;
        this.dlvPrice = dlvPrice;
        this.totalPayPrice = totalPayPrice;
        this.pwd = pwd;
        this.regId = regId;
        this.upId = upId;
    }

    public OrderDto(int ordId, int custId, String ordStusCode, String codeType, String custChk, String ordDate, String ordReqMsg, int totalProdPrice, int totalDiscPrice, int dlvPrice, int totalPayPrice, String pwd, String regDate, String regId, String upDate, String upId) {
        this.ordId = ordId;
        this.custId = custId;
        this.ordStusCode = ordStusCode;
        this.codeType = codeType;
        this.custChk = custChk;
        this.ordDate = ordDate;
        this.ordReqMsg = ordReqMsg;
        this.totalProdPrice = totalProdPrice;
        this.totalDiscPrice = totalDiscPrice;
        this.dlvPrice = dlvPrice;
        this.totalPayPrice = totalPayPrice;
        this.pwd = pwd;
        this.regDate = regDate;
        this.regId = regId;
        this.upDate = upDate;
        this.upId = upId;
    }

    public int getOrdId() {
        return ordId;
    }

    public int getCustId() {
        return custId;
    }

    public String getOrdStusCode() {
        return ordStusCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public String getCustChk() {
        return custChk;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public String getOrdReqMsg() {
        return ordReqMsg;
    }

    public int getTotalProdPrice() {
        return totalProdPrice;
    }

    public int getTotalDiscPrice() {
        return totalDiscPrice;
    }

    public int getDlvPrice() {
        return dlvPrice;
    }

    public int getTotalPayPrice() {
        return totalPayPrice;
    }

    public String getPwd() {
        return pwd;
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
}
