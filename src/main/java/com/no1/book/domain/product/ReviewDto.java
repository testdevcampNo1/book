package com.no1.book.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReviewDto {
    Integer reviewId;
    String prodId;
    String custId;
    String content;
    Integer rcmdCount;
    Integer starPt;
    String regDate;
    String regId;
    String upDate;
}
