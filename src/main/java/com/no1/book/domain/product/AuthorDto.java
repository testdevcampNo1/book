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
public class AuthorDto {

    private String authorInfoId;
    private String authorName;
    private String authorBornDate;
    private String authorBio;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;
}