package com.no1.book.domain;

public class TestDto {
    String num;

    public TestDto(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "num='" + num + '\'' +
                '}';
    }
}
