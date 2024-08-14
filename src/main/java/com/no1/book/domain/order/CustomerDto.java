package com.no1.book.domain.order;

import lombok.Data;

@Data
public class CustomerDto {
    private int custId = 1;
    private String pwd = "1234";
    private String name = "한지선";
    private String mainAddr = "1";
    private String mobileNum = "010-1234-5678";
    private String email = "seon.hannn@gmail.com";
    private String birthDate = "2002-01-08";
    private String date = "";
    private String image = "";
    private String nickname = "지선이";
    private int accPrice;
    private int point = 0;
    private String tou;
    private String adultChk = "Y";
    private String withdChk;
    private int gradeId;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;
    private String gender = "F";
}
