package com.no1.book.domain.customer;


import lombok.Data;

@Data
public class UpdateMemberRequestDto {
    private String pwd;
    private String mobileNum;

}
