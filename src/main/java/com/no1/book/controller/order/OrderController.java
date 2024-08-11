package com.no1.book.controller.order;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.domain.customer.DeliveryAddressDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/orderForm")
    public String orderForm(Model model) {

        model.addAttribute("OrderFormDto", getOrderForm());
        model.addAttribute("orderInfo", getOrderForm());
        model.addAttribute("productList", getOrderForm().getProductList());
        return "orderFormTemp";
    }

    // test order
    OrderFormDto getOrderForm() {
        DeliveryAddressDto deliveryAddressDto = new DeliveryAddressDto();
        OrderFormDto orderFormDto = new OrderFormDto();
        CustomerDto customerDto = new CustomerDto();
        // 고객
        orderFormDto.setEmail(customerDto.getEmail());
        orderFormDto.setName(customerDto.getName());
        // 배송
        orderFormDto.setCustId(1);
        orderFormDto.setAddressName(deliveryAddressDto.getName());
        orderFormDto.setTelNum(deliveryAddressDto.getMobileNum());
        orderFormDto.setZipCode(deliveryAddressDto.getZpcd());
        orderFormDto.setMainAddress(deliveryAddressDto.getMainAddr());
        orderFormDto.setDetailAddress(deliveryAddressDto.getDetailAddr());
        orderFormDto.setDefaultChk(deliveryAddressDto.getDefaultChk());
        // 상품
        orderFormDto.setProductList(getOrderProductDtoList());
        return orderFormDto;
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        OrderProductDto orderProductDto = new OrderProductDto(1, 1, "100", "주문가능", "301", "N", "[국내도서] Java의 정석:기초편 세트", 2, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788994492049.jpg", "https://product.kyobobook.co.kr/detail/S000001550353", "N", 50000, 5000, 45000, "1", "1");
        OrderProductDto orderProductDto2 = new OrderProductDto(2, 1, "101", "주문가능", "301", "N", "[국내도서] 토비의 스프링 3.1 세트", 1, "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788960773431.jpg", "https://product.kyobobook.co.kr/detail/S000000935360", "N", 75000, 7500, 67500, "1", "1");
        orderProductDtoList.add(orderProductDto);
        orderProductDtoList.add(orderProductDto2);
        return orderProductDtoList;
    }
}
