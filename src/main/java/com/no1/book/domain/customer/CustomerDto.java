package com.no1.book.domain.customer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Getter @Setter
public class CustomerDto {
    private String custId;      //회원ID (PK)
    private String pwd;
    private String name;
    private String mainAddr;
    private String mobileNum;
    private String gender;
    private String email;
    private String birthDate;
    private String date;
    private String image;
    private String nickName;
    private String accPrice;
    private String point;
    private String tou;
    private String adultChk;
    private String withdChk;
    private int gradeId;
    private String regDate;
    private String regId;
    private String upDate;
    private String upId;



    public CustomerDto() {}

    public CustomerDto(String custId, String pwd, String name, String mainAddr, String mobileNum, String gender, String email, String birthDate, String date, String image, String nickName, String accPrice, String point, String tou, String adultChk, String withdChk, int gradeId, String regDate, String regId, String upDate, String upId) {
        this.custId = custId;
        this.pwd = pwd;
        this.name = name;
        this.mainAddr = mainAddr;
        this.mobileNum = mobileNum;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
        this.date = date;
        this.image = image;
        this.nickName = nickName;
        this.accPrice = accPrice;
        this.point = point;
        this.tou = tou;
        this.adultChk = adultChk;
        this.withdChk = withdChk;
        this.gradeId = gradeId;
        this.regDate = regDate;
        this.regId = regId;
        this.upDate = upDate;
        this.upId = upId;

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto customerDto = (CustomerDto) o;
        return custId.equals(customerDto.custId) && Objects.equals(pwd, customerDto.pwd) && Objects.equals(name, customerDto.name) && Objects.equals(email, customerDto.email);
    }

    public String getCustId() {
        return custId;
    }

    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public String getMainAddr() {
        return mainAddr;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getNickName() {
        return nickName;
    }

    public String getAccPrice() {
        return accPrice;
    }

    public String getPoint() {
        return point;
    }

    public String getTou() {
        return tou;
    }

    public String getAdultChk() {
        return adultChk;
    }

    public String getWithdChk() {
        return withdChk;
    }

    public int getGradeId() {
        return gradeId;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getRegId() {
        return regId;
    }

    public String getUpDate() {
        return upDate;
    }

    public String getUpId() {
        return upId;
    }
}
