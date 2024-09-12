package com.no1.book.controller.order;

import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.service.order.OrderService;
import com.no1.book.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/form")
    public ResponseEntity<Map<String, Object>> order(@RequestBody List<OrderProductDto> orderProductDtoList, HttpSession session) {
        session.setAttribute("orderProductDtoList", orderProductDtoList);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        return new ResponseEntity<>(response, HttpStatus.valueOf(200));
    }

    // 상품상세 또는 장바구니 화면에서 진입하는 주문 화면
    @GetMapping("/order/form")
    public String orderForm(HttpSession session, Model model) throws Exception {
        // session에서 data 조회
        String custId = (String) session.getAttribute("custId");
        List<OrderProductDto> orderProductDtoList = (List<OrderProductDto>) session.getAttribute("orderProductDtoList");

        // 불필요한 session 정보 제거
        session.removeAttribute("orderProductDtoList");

        OrderFormDto orderFormDto = orderService.initOrderInfo(custId, orderProductDtoList);

        model.addAttribute("orderFormDto", orderFormDto);
        model.addAttribute("productList", orderFormDto.getProductList());

        return "order/orderForm";
    }
}

