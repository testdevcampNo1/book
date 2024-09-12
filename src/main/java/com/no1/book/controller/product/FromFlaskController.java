package com.no1.book.controller.product;

import com.no1.book.domain.order.CartDto;
import com.no1.book.service.order.CartService;
import com.no1.book.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FromFlaskController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @PostMapping("/receive-data")
    public String receiveDataFromFlask(@RequestBody Map<String, Object> data) {
        System.out.println("Received data from Flask: " + data);
        return "Data received successfully";
    }

    @PostMapping("/receive-cart")
    public ResponseEntity<String> receiveCart(@RequestBody Map<String, Object> data) {
        // 수신된 데이터 로깅
        System.out.println("Received data from Flask: " + data);

        try {
            // cust_id와 prod_id가 있는지 확인
            if (data.containsKey("cust_id") && data.containsKey("prod_id")) {
                String custId = data.get("cust_id").toString();
                String prodName = data.get("prod_name").toString();

                String prodId = productService.selectIdByName(prodName);

                CartDto cartDto = new CartDto(custId, prodId, 1); // 여기에 prodName이 아니라 prodId가 들어가야 함...
                // prodName으로 prodId 가져오는 쿼리로 해결해야 할 듯?
                // 장바구니 안에 이미 있든 없든, 사용자가 2개 3개를 넣어달라 하든 일단 수량은 1개로 하자

                // 장바구니에 추가하는 로직 호출
                Integer success = cartService.insertItem(cartDto);

                // 성공 여부 확인
                if (success != null && success > 0) {
                    return ResponseEntity.ok("장바구니에 성공적으로 추가되었습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("장바구니에 추가하지 못했습니다.");
                }
            } else {
                return ResponseEntity.badRequest().body("필수 데이터 누락: cust_id 또는 prod_id");
            }
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 에러 응답 반환
            System.err.println("Error processing cart: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버에서 오류가 발생했습니다.");
        }
    }
}