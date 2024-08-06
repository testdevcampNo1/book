package com.no1.book.mapper;

import com.no1.book.domain.TestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    String now();
    TestDto select();
}