package com.no1.book.controller.order;

import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.service.order.OrderService;
import com.no1.book.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/payment")
@Controller
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPayment(@RequestBody OrderFormDto orderFormDto, HttpSession session) {
        session.setAttribute("orderFormDto", orderFormDto);
        return ResponseEntity.ok("success request payment");
    }

    @GetMapping("/request")
    public String payment(HttpSession session, Model model) {
        OrderFormDto orderFormDto = (OrderFormDto) session.getAttribute("orderFormDto");
        model.addAttribute("orderFormDto", orderFormDto);
        return "/order/requestPayment";
    }

    @GetMapping("/complete")
    public String completePayment(Model model, HttpSession session) {

        OrderFormDto orderFormDto = (OrderFormDto) session.getAttribute("orderFormDto");
        orderService.requestOrder(orderFormDto);
        model.addAttribute("orderFormDto", orderFormDto);

        session.removeAttribute("orderFormDto");

        return "/order/orderComplete";
    }
}
