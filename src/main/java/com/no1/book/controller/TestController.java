package com.no1.book.controller;

import com.no1.book.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Autowired
    TestService testService;

    @RequestMapping("test")
    public String test(Model m){
        m.addAttribute("select", testService.select());
        return "test";
    }
}
