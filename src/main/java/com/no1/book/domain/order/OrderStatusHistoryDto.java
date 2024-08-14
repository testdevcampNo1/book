package com.no1.book.domain.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderStatusHistoryDto {
    private int ordStusHistorySeq;
    private int ordId;
    private String befOrdStusCode;
    private String currOrdStusCode;
    private String chgStusReason;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;

    // ordStusHistorySeq - auto increment
    // regDate, upDate - 현재시간으로 추가
    public OrderStatusHistoryDto(int ordId, String befOrdStusCode, String currOrdStusCode, String chgStusReason, String regId, String upId) {
        this.ordId = ordId;
        this.befOrdStusCode = befOrdStusCode;
        this.currOrdStusCode = currOrdStusCode;
        this.chgStusReason = chgStusReason;
        this.regId = regId;
        this.upId = upId;
    }

    public int getOrdStusHistorySeq() {
        return ordStusHistorySeq;
    }

    public int getOrdId() {
        return ordId;
    }

    public String getBefOrdStusCode() {
        return befOrdStusCode;
    }

    public String getCurrOrdStusCode() {
        return currOrdStusCode;
    }

    public String getChgStusReason() {
        return chgStusReason;
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
