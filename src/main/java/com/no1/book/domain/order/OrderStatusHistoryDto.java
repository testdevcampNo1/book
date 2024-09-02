package com.no1.book.domain.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    // ordStusHistorySeq - auto increment
}
