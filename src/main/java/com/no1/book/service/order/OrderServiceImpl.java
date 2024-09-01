package com.no1.book.service.order;

import com.no1.book.common.exception.order.InvalidOrderException;
import com.no1.book.common.exception.order.InvalidProductException;
import com.no1.book.common.exception.order.OrderValidatorErrorMessage;
import com.no1.book.common.exception.order.SystemException;
import com.no1.book.common.validator.order.OrderValidator;
import com.no1.book.dao.customer.CustomerDao;
import com.no1.book.dao.order.DeliveryAddressDao;
import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.dao.order.OrderStatusHistoryDao;
import com.no1.book.dao.product.ProductDao;
import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.domain.order.DeliveryAddressDto;
import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.domain.order.OrderStatusHistoryDto;
import com.no1.book.domain.product.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderStatusHistoryDao orderStatusHistoryDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private DeliveryAddressDao deliveryAddressDao;

    @Autowired
    private ProductDao productDao;

    private final OrderValidator orderValidator = new OrderValidator();

    @Value("${MIN_ORDER_AMOUNT_FOR_FREE_DELIVERY}")
    private int minOrderAmountForFreeDelivery;

    @Value("${DELIVERY_FEE}")
    private int deliveryFee;

    // 주문 화면 진입시 노출할 정보
    @Override
    public OrderFormDto initOrderInfo(int custId, List<OrderProductDto> productList) throws Exception {
        OrderFormDto orderFormDto = new OrderFormDto();
        orderFormDto.setCustId(custId);

        if(custId > -1) {
            // 회원 정보, 기본 배송지 조회
            getCustomerInfo(custId, orderFormDto);
            getAddressInfo(custId, orderFormDto);
        }

        // 상품 정보 가공하여 저장
        orderFormDto.setProductList(productList);
        setProductInfo(orderFormDto);
        setDeliveryDate(orderFormDto);
        setDeliveryPrice(orderFormDto);

        return orderFormDto;
    }

    // 회원 정보 DB 조회
    void getCustomerInfo(int custId, OrderFormDto orderFormDto) {
        try {
            CustomerDto customerDto = customerDao.selectCustomer(String.valueOf(custId));

            orderFormDto.setEmail(customerDto.getEmail()); // 회원 이메일
            orderFormDto.setName(customerDto.getName()); // 회원 이름
        } catch (DataAccessException e) {
            throw new SystemException("회원 정보 조회 실패했습니다.");
        }
    }

    // 기본배송지 정보 DB 조회
    int getAddressInfo(int custId, OrderFormDto orderFormDto) {
        try {
            DeliveryAddressDto deliveryAddressDto = deliveryAddressDao.getDefaultAddress(custId);

            orderFormDto.setAddressName(deliveryAddressDto.getName()); // 배송지 이름
            orderFormDto.setTelNum(deliveryAddressDto.getMobileNum()); // 휴대전화번호
            orderFormDto.setZipCode(deliveryAddressDto.getZpcd()); // 우편번호
            orderFormDto.setMainAddress(deliveryAddressDto.getMainAddr()); // 기본주소
            orderFormDto.setDetailAddress(deliveryAddressDto.getDetailAddr()); // 상세주소
            orderFormDto.setDefaultChk(deliveryAddressDto.getDefaultChk()); // 기본배송지 여부

            return deliveryAddressDto.getDlvId();
        } catch (DataAccessException e) {
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }
    }

    // 기본배송지 갱신
    void updateDefaultAddress(OrderFormDto orderFormDto) {
        int dlvId = getAddressInfo(orderFormDto.getCustId(), orderFormDto);

        DeliveryAddressDto deliveryAddressDto = DeliveryAddressDto.builder()
                .dlvId(dlvId)
                .zpcd(orderFormDto.getZipCode())
                .mainAddr(orderFormDto.getMainAddress())
                .detailAddr(orderFormDto.getDetailAddress())
                .mobileNum(orderFormDto.getTelNum())
                .upId(String.valueOf(orderFormDto.getCustId()))
                .build();

        try {
            deliveryAddressDao.updateDefaultAddress(deliveryAddressDto);
        } catch (DataAccessException e) {
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }
    }

    // 상품 정보 저장
    void setProductInfo(OrderFormDto orderFormDto) throws Exception {
        int totalProdBasePrice = 0;
        int totalDiscPrice = 0;
        int totalOrderQuantity = 0;

        boolean isAllDawnDelivery = true;
        boolean isAllEbook = true;

        for(OrderProductDto product : orderFormDto.getProductList()) {
            // 금액
            totalProdBasePrice += product.getProdBasePrice() * product.getOrdQty();
            totalDiscPrice += product.getDiscPrice() * product.getOrdQty();

            // 수량
            totalOrderQuantity += product.getOrdQty();

            if(product.getDawnDeliChk().equals("N")) {
                isAllDawnDelivery = false;
            }

            if(product.getIsEbook().equals("N")) {
                isAllEbook = false;
            }

            // 상품 상태 조회
            if(!isProductAvailable(product.getProdId())) throw new InvalidOrderException("구매 불가능한 상품입니다. " + product.getProdId());

            // 상품 금액 변동 여부 조회
            if(isChangeProductPrice(product.getProdId(), product.getProdBasePrice())) throw new InvalidOrderException("구매 불가능한 상품입니다. " + product.getProdId());
        }

        orderFormDto.setTotalProdBasePrice(totalProdBasePrice);
        orderFormDto.setTotalDiscPrice(totalDiscPrice);
        orderFormDto.setTotalOrdQty(totalOrderQuantity);
        orderFormDto.setIsAllDawnDelivery(isAllDawnDelivery ? "Y" : "N");
        orderFormDto.setIsAllEbook(isAllEbook ? "Y" : "N");
    }

    // 예상 배송일 계산
    void setDeliveryDate(OrderFormDto orderFormDto) {
        /*
        배송일 - Ebook, 새벽배송 여부에 따라 달라진다.
        1. 모두 Ebook -> 배송일 : 바로 다운로드 가능
        2. 일부 Ebook 또는 모두 일반 책, 모두 새벽배송 -> 배송일 : 24시간 이내 배송
        3. 일부 Ebook 또는 모두 일반 책, 일부 새벽배송 -> 배송일 : 48시간 이내 배송
         */
        if(orderFormDto.getIsAllEbook().equals("Y")) {
            orderFormDto.setDlvDate("모두 Ebook 상품으로, 바로 다운로드 가능합니다.");
        } else if(orderFormDto.getIsAllDawnDelivery().equals("Y")) {
            orderFormDto.setDlvDate("모두 새벽배송 상품으로, 24시간 이내 배송 예정입니다.");
        } else {
            orderFormDto.setDlvDate("48시간 이내 배송 예정입니다.");
        }
    }

    // 배송비 계산
    void setDeliveryPrice(OrderFormDto orderFormDto) {
        int dlvPrice = orderFormDto.getTotalProdBasePrice() - orderFormDto.getTotalDiscPrice() < minOrderAmountForFreeDelivery ? deliveryFee : 0;

        orderFormDto.setTotalPayPrice(orderFormDto.getTotalProdBasePrice() - orderFormDto.getTotalDiscPrice() - dlvPrice);
        orderFormDto.setDlvPrice(dlvPrice);
    }

    // 상품 상태 검증
    @Transactional
    boolean isProductAvailable(String prodId) throws Exception {
        ProductDto productDto = new ProductDto();

        // 상품 상태 조회
        try {
            productDto.setOrdChkCode(productDao.select(prodId).getOrdChkCode());
        } catch (DataAccessException e) {
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }

        // 구매 가능 상태 검증
        if(!productDto.getOrdChkCode().equals("AVBL")) {
            throw new InvalidProductException(OrderValidatorErrorMessage.INVALID_PRODUCT_STATUS.getMessage());
        }

         return true;
    }

    // 상품 금액 변동 여부 검증
    boolean isChangeProductPrice(String prodId, int prodBasePrice) throws Exception {
        ProductDto productDto = new ProductDto();

        // 상품 금액 조회
        try {
            productDto.setProdBasePrice(productDao.select(prodId).getProdBasePrice());
        } catch (DataAccessException e) {
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }

        // 상품 가격 변동 여부 검증
        if(productDto.getProdBasePrice() != prodBasePrice) {
            throw new InvalidProductException(OrderValidatorErrorMessage.CHANGE_PRODUCT_PRICE.getMessage());
        }

        return false;
    }

    // 주문 번호 생성
    public synchronized String orderNumGenerator() {
        return UUID.randomUUID().toString();
    }

    // 주문 요청
    public void requestOrder(OrderFormDto orderFormDto) {
        saveOrder(orderFormDto);
        saveOrderProduct(orderFormDto.getOrdId(), orderFormDto.getProductList());
        saveOrderStatus(orderFormDto.getOrdId());
        saveDelivery(orderFormDto.getOrdId());
        savePayment(orderFormDto.getOrdId());

        if(orderFormDto.getCustId() != -1 && orderFormDto.getDefaultChk().equals("Y")) {
            updateDefaultAddress(orderFormDto);
        }
    }

    // 특정 주문의 regId 조회
    public String getRegId(String ordId) {
        String regId = "";
        try {
            regId = orderDao.getOrder(ordId).getRegId();
        } catch (DataAccessException e) {
            throw new SystemException(e.getMessage());
        }

        return regId;
    }

    @Transactional
    @Override
    public void saveOrder(OrderFormDto orderFormDto) {
        orderValidator.validateOrder(orderFormDto);

        String ordId = orderNumGenerator();
        orderFormDto.setOrdId(ordId);
        String regId = orderFormDto.getCustId() > -1 ? String.valueOf(orderFormDto.getCustId()) : orderFormDto.getEmail();
        String custCheck = orderFormDto.getCustId() > -1 ? "Y" : "N";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        OrderDto orderDto = OrderDto.builder()
                .ordId(ordId)
                .custId(orderFormDto.getCustId())
                .custChk(custCheck)
                .pwd(orderFormDto.getPwd())
                .ordStusCode("RCVD")
                .codeType("301")
                .ordReqMsg(orderFormDto.getOrderRequestMessage())
                .ordDate(date)
                .totalProdPrice(orderFormDto.getTotalProdBasePrice())
                .totalDiscPrice(orderFormDto.getTotalDiscPrice())
                .dlvPrice(orderFormDto.getDlvPrice())
                .totalPayPrice(orderFormDto.getTotalPayPrice())
                .regId(regId)
                .upId(regId)
                .build();

        try {
            orderDao.createOrder(orderDto);
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }
    }

    @Transactional
    @Override
    public void saveOrderProduct(String ordId, List<OrderProductDto> productList) {
        String regId = getRegId(ordId);

        try {
            for(OrderProductDto product : productList) {
                product.setOrdId(ordId);
                product.setRegId(regId);
                product.setUpId(regId);

                orderProductDao.insertOrderProduct(product);
            }
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }
    }

    @Transactional
    @Override
    public void saveOrderStatus(String ordId) {
        String regId = getRegId(ordId);

        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "RCVD", "결제 승인", regId, regId);

        try {
            orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException(OrderValidatorErrorMessage.ACCESS_DATABASE_FAILED.getMessage());
        }
    }

    // 배송 DB 저장
    @Transactional
    @Override
    public void saveDelivery(String ordId) {
    }

    // 결제 DB 저장
    @Transactional
    @Override
    public void savePayment(String ordId) {
    }
}
