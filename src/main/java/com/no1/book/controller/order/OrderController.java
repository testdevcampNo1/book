package com.no1.book.controller.order;

import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.domain.product.ProductDto;
import com.no1.book.service.order.OrderService;
import com.no1.book.service.product.ProductService;
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

    @Autowired
    private ProductService productService;

    // 상품상세 또는 장바구니 화면에서 진입하는 주문 화면
    @GetMapping("/orderForm")
    public String orderForm(Model model) {
        // TODO : 상품상세, 장바구니 화면 구현 후 실제 data 삽입 필요
        System.out.println(getOrderProductDtoList());
        OrderFormDto orderFormDto = orderService.initOrderInfo(1, getOrderProductDtoList());

        model.addAttribute("orderFormDto", orderFormDto);
        model.addAttribute("productList", orderFormDto.getProductList());

        return "/order/orderForm";
    }

    // 주문왼료화면
    @PostMapping("/orderComplete")
    public String order(OrderFormDto orderFormDto, Model model) {
        orderService.requestOrder(orderFormDto);

        model.addAttribute("orderFormDto", orderFormDto);
        model.addAttribute("productList", orderFormDto.getProductList());
        model.addAttribute("ordId", orderFormDto.getOrdId());

        return "/order/orderComplete";
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();

        try {
            productDto1 = productService.readProductDetail("PROD001");
            productDto2 = productService.readProductDetail("PROD002");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OrderProductDto ordProd1 = OrderProductDto.builder()
                .ordProdId(15)
                .prodId(productDto1.getProdId())
                .ordChkCode(productDto1.getOrdChkCode())
                .codeType(productDto1.getCodeType())
                .isEbook(productDto1.getIsEbook() == "1" ? "Y" : "N")
                .dawnDeliChk(productDto1.getDawnDeliChk())
                .prodName(productDto1.getProdName())
                .img("https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788994492049.jpg")
                .prodPageLink("")
                .ordQty(5)
                .prodBasePrice(productDto1.getProdBasePrice())
                .totalProdPrice(productDto1.getProdBasePrice() * 5)
                .discPrice(productDto1.getDiscPrice())
                .totalDiscPrice(productDto1.getDiscPrice() * 5)
                .totalPayPrice(5 * productDto1.getProdBasePrice() - 5 * productDto1.getDiscPrice())
                .regId("1")
                .upId("1")
                .build();

        OrderProductDto ordProd2 = OrderProductDto.builder()
                .ordProdId(18)
                .prodId(productDto2.getProdId())
                .ordChkCode(productDto2.getOrdChkCode())
                .codeType(productDto2.getCodeType())
                .isEbook(productDto2.getIsEbook())
                .dawnDeliChk(productDto2.getDawnDeliChk())
                .prodName(productDto2.getProdName())
                .img("https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788994492049.jpg")
                .prodPageLink("")
                .ordQty(3)
                .prodBasePrice(productDto2.getProdBasePrice())
                .totalProdPrice(productDto2.getProdBasePrice() * 3)
                .discPrice(productDto2.getDiscPrice())
                .totalDiscPrice(productDto2.getDiscPrice() * 3)
                .totalPayPrice(5 * productDto2.getProdBasePrice() - 5 * productDto2.getDiscPrice())
                .regId("1")
                .upId("1")
                .build();

        orderProductDtoList.add(ordProd1);
        orderProductDtoList.add(ordProd2);

        return orderProductDtoList;
    }
}
