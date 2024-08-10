package com.no1.book.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDto {
    // 필드 선언부
    private String prodId;
    @Builder.Default
    private Boolean isEbook = false;
    private String prodName;
    private Integer prodBasePrice;
    private Integer discRate;
    private Integer discPrice;
    private Integer salePrice;
    private Integer totalSales;
    private String tableOfContent;
    private String smry;
    private String pblcr;
    private String pblcrReview;
    private String imageId;
    private String isbn;
    private String pblshDate;
    private String totalPages;
    private String totalBooks;
    private String trlr;
    private String dawnDeliChk;
    private Float starAvg;
    private String cateCode;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;
    @Builder.Default
    private String authorInfoId = "0";
    @Builder.Default
    private String ordChkCode = "0";
    @Builder.Default
    private String codeType = "0";

//    public ProductDto (String proId, boolean isEbook, String prodName, int prodBasePrice, int totalSales,
//                       String cateId, String authorInfoId, String ordChkCode, String codeType) {
//        this.prodId = proId;
//        this.isEbook = isEbook;
//        this.prodName = prodName;
//        this.prodBasePrice = prodBasePrice;
//        this.cateId = cateId;
//        this.authorInfoId = authorInfoId;
}
