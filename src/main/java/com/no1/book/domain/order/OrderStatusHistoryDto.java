package com.no1.book.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatusHistoryDto {
    private int ordStusHistorySeq;
    private String ordId;
    private String befOrdStusCode;
    private String currOrdStusCode;
    private String chgStusReason;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;

    @Builder
    public OrderStatusHistoryDto(String ordId, String befOrdStusCode, String currOrdStusCode, String chgStusReason, String regId, String upId) {
        this.ordId = ordId;
        this.befOrdStusCode = befOrdStusCode;
        this.currOrdStusCode = currOrdStusCode;
        this.chgStusReason = chgStusReason;
        this.regId = regId;
        this.upId = upId;
    }
}
