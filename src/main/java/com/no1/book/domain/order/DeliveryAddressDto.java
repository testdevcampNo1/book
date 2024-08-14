package com.no1.book.domain.order;

import lombok.Data;

@Data
public class DeliveryAddressDto {
    private int dlvId = 1;
    private String name = "학원";
    private String zpcd = "06241";
    private String mainAddr = "서울특별시 강남구 강남대로 364 (역삼동, 미왕빌딩)";
    private String detailAddr = "10C 강의장";
    private String mobileNum = "010-1234-5678";
    private String defaultChk = "Y";
    private int custId = 1;
    private String regDate = "2020-01-01";
    private String regId = "1";
    private String upDate = "2020-01-01";
    private String upId = "1";
}
