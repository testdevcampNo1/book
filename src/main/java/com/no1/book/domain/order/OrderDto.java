package com.no1.book.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {
    private String ordId; // auto increment
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

    // regDate, upDate, ordDate - 현재시간으로 추가
    public OrderDto(String ordId, int custId, String ordStusCode, String codeType, String custChk, String ordReqMsg, int totalProdPrice, int totalDiscPrice, int dlvPrice, int totalPayPrice, String pwd, String regId, String upId) {
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
}
