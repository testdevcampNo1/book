package com.no1.book.controller.product;

import com.no1.book.domain.product.PageHandler;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
1. 리스트부터 띄워 보자
2.

 */

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public String list(Integer page, Integer pageSize, Model m) {

        if (page==null) page=1;
        if (pageSize==null) pageSize=10;

        try {
            int totalCnt = productService.getProductCount();
            PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

            Map map = new HashMap();
            map.put("offset", (page - 1) * pageSize);
            map.put("pageSize", pageSize);

            List<ProductDto> list = productService.getPage(map);
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);
            m.addAttribute("page", pageHandler);
            m.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "productList";
    }
}
