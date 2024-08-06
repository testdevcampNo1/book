package com.no1.book.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class testMapperTest {

    @Autowired
    TestMapper testMapper;

    @Test
    void test(){
        System.out.println("현재 시간 : " + testMapper.now());
        System.out.println(testMapper.select());

    }
}