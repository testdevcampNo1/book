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
    Integer reviewId;       // 리뷰 ID
    String prodId;          // 상품 ID
    String custId;          // 고객 ID
    String content;         // 리뷰 내용
    Integer rcmdCount;      // 추천 수
    Integer starPt;         // 별점
    String regDate;         // 등록일
    String regId;           // 등록자 ID
    String upDate;          // 수정일
    String sentiment;       // 감정 상태 (positive, negative, pending)
}