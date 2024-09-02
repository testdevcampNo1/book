package com.no1.book.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductDto {
    private String custId;
    private String prodId;
    private int reviewCnt=0;

    public CustomerProductDto(String custId, String prodId) {
        this.custId = custId;
        this.prodId = prodId;
    }
}