package com.no1.book.controller.order;

import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.service.order.OrderHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@Controller
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @GetMapping("/history")
    public String orderHistoryList(Model model, HttpSession session) {
        String custId = (String) session.getAttribute("custId");
        Map<String, List<OrderProductDto>> orderHistory = orderHistoryService.getCustomerOrderHistoryList(custId);
        model.addAttribute("orderHistory", orderHistory);
        return "/order/orderHistory";
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(int ordProdId) {
        try {
            orderHistoryService.cancelOrder(ordProdId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
