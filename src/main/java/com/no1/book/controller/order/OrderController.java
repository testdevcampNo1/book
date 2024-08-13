package com.no1.book.controller.order;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.domain.customer.DeliveryAddressDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orderForm")
    public String orderForm(Model model) {
        // 상품상세 또는 장바구니 화면에서 진입하는 주문 화면

        // 1. 상품 목록, 주문 상품 개수, 회원ID를 전달받는다.
        // TODO : 상품상세, 장바구니 화면 구현 후 실제 data 삽입 필요
        OrderFormDto orderInfo = orderService.initOrderInfo(1, getOrderProductDtoList());

        // 2. 주문 화면에 출력하기 위해 model에 OrderFormDto를 저장한다.
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("productList", orderInfo.getProductList());

        return "orderForm";
    }

    @PostMapping("/orderComplete")
    public String order(OrderFormDto orderInfo, Model model) {
        // 결제 완료 후 진입하는 주문왼료화면

        // 0. 결제 성공
        // 1. 주문 table에 저장한다.
        orderService.saveOrder(orderInfo);

        // 2. 가장 최근의 주문ID를 조회한다.
        int ordId = orderService.getOrderId();

        // 3. 주문 관련 데이터를 DB에 저장한다.
            // table - 주문상품, 주문상태이력, 배송, 결제
        orderService.saveOrderStatus();
        orderService.saveOrderProduct(orderInfo.getProductList());
        orderService.saveDelivery();
        orderService.savePayment();

        // 3. 전달받은 주문 form 화면의 정보, 주문 번호를 주문완료 화면에 출력하기 위해 model에 저장한다.
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("productList", orderInfo.getProductList());
        model.addAttribute("ordId", ordId);

        return "orderComplete";
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto = new OrderProductDto(1, -1, "100", "AVBL", "301", "N", "[국내도서] Java의 정석:기초편 세트", 2, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788994492049.jpg", "https://product.kyobobook.co.kr/detail/S000001550353", "N", 50000, 5000, 45000, "1", "1");
        OrderProductDto orderProductDto2 = new OrderProductDto(2, -1, "101", "AVBL", "301", "N", "[국내도서] 토비의 스프링 3.1 세트", 1, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788960773431.jpg", "https://product.kyobobook.co.kr/detail/S000000935360", "N", 75000, 7500, 67500, "1", "1");
        orderProductDtoList.add(orderProductDto);
        orderProductDtoList.add(orderProductDto2);
        return orderProductDtoList;
    }
}
