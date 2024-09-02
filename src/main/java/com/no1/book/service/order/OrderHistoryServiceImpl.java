package com.no1.book.service.order;

import com.no1.book.common.exception.order.SystemException;
import com.no1.book.common.exception.order.OrderValidatorErrorMessage;
import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.domain.order.OrderProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Override
    public Map<String, List<OrderProductDto>> getCustomerOrderHistoryList(String custId) {
        List<OrderProductDto> orderProductList = orderProductDao.getCustomerOrderProducts(custId);
        // ordId로 그룹핑
        var res = orderProductList.stream().collect(Collectors.groupingBy(OrderProductDto::getOrdId));
        return res;
    }

    @Override
    public OrderProductDto getOrderProductDto(int ordProdId) {
        try {
            OrderProductDto orderProductDto = orderProductDao.getOrderProduct(ordProdId);
            return orderProductDto;
        } catch (DataAccessException e) {
            throw new SystemException(OrderValidatorErrorMessage.MISSING_PRODUCT_ID.getMessage());
        }
    }

    @Override
    public void cancelOrder(int ordProdId) {
        OrderProductDto orderProductDto = getOrderProductDto(ordProdId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cancelableDate = LocalDateTime.parse(orderProductDto.getCancelableDate(), formatter);

        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(cancelableDate) && orderProductDto.getOrdChkCode().equals("AVBL")) {
            try {
                Map params = new HashMap();
                params.put("ordProdId", ordProdId);
                params.put("ordChkCode", "CNCL");
                params.put("upId", orderProductDto.getRegId());
                orderProductDao.updateOrderProductStatus(params);
            } catch (DataAccessException e) {
                throw new SystemException(e.getMessage());
            }
        }
    }
}