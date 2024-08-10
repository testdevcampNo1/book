package com.no1.book.service;

import com.no1.book.domain.TestDto;
import com.no1.book.dao.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    TestMapper testMapper;

    public TestDto select(){
        return testMapper.select();
    }
}
