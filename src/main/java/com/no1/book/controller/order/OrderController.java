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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/order")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    // 상품상세 또는 장바구니 화면에서 진입하는 주문 화면
    @GetMapping("/form")
    public String orderForm(Model model) {
        // TODO : 상품상세, 장바구니 화면 구현 후 실제 data 삽입 필요
        System.out.println(getOrderProductDtoList());
        OrderFormDto orderFormDto = orderService.initOrderInfo(1, getOrderProductDtoList());

        model.addAttribute("orderFormDto", orderFormDto);
        model.addAttribute("productList", orderFormDto.getProductList());

        return "/order/orderForm";
    }

    // test product
    List<OrderProductDto> getOrderProductDtoList() {
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();

        int prod1Qty = 1;
        int prod2Qty = 5;

        try {
            productDto1 = productService.readProductDetail("PROD003");
            productDto2 = productService.readProductDetail("PROD004");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OrderProductDto ordProd1 = OrderProductDto.builder()
                .ordProdId(20)
                .prodId(productDto1.getProdId())
                .ordChkCode(productDto1.getOrdChkCode())
                .codeType(productDto1.getCodeType())
                .isEbook(productDto1.getIsEbook().equals("1") ? "Y" : "N")
                .dawnDeliChk(productDto1.getDawnDeliChk())
                .prodName(productDto1.getProdName())
                .img("https://dhub.dgist.ac.kr/resources/images/common/no_img.jpg")
                .prodPageLink("")
                .ordQty(prod1Qty)
                .prodBasePrice(productDto1.getProdBasePrice())
                .totalProdPrice(productDto1.getProdBasePrice() * prod1Qty)
                .discPrice(productDto1.getDiscPrice())
                .totalDiscPrice(productDto1.getDiscPrice() * prod1Qty)
                .totalPayPrice(productDto1.getProdBasePrice() * prod1Qty - productDto1.getDiscPrice() * prod1Qty)
                .regId("1")
                .upId("1")
                .build();

        OrderProductDto ordProd2 = OrderProductDto.builder()
                .ordProdId(21)
                .prodId(productDto2.getProdId())
                .ordChkCode(productDto2.getOrdChkCode())
                .codeType(productDto2.getCodeType())
                .isEbook(productDto2.getIsEbook().equals("1") ? "Y" : "N")
                .dawnDeliChk(productDto2.getDawnDeliChk())
                .prodName(productDto2.getProdName())
                .img("https://dhub.dgist.ac.kr/resources/images/common/no_img.jpg")
                .prodPageLink("")
                .ordQty(prod2Qty)
                .prodBasePrice(productDto2.getProdBasePrice())
                .totalProdPrice(productDto2.getProdBasePrice() * prod2Qty)
                .discPrice(productDto2.getDiscPrice())
                .totalDiscPrice(productDto2.getDiscPrice() * prod2Qty)
                .totalPayPrice(productDto2.getProdBasePrice() * prod2Qty - productDto2.getDiscPrice() * prod2Qty)
                .regId("1")
                .upId("1")
                .build();

        orderProductDtoList.add(ordProd1);
        orderProductDtoList.add(ordProd2);

        return orderProductDtoList;
    }
}
