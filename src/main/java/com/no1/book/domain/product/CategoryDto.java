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
public class CategoryDto {
    private String cateCode;   // 010101
    private String cateName;   //  일반책>국내도서>시/에세이
    private String cateNow;     // 시/에세이
}