package com.no1.book.service.order;

import com.no1.book.common.exception.order.OrderException;
import com.no1.book.common.exception.order.OrderValidatorErrorMessage;
import com.no1.book.common.exception.order.SystemException;
import com.no1.book.common.validator.order.OrderValidator;
import com.no1.book.dao.order.OrderDao;
import com.no1.book.dao.order.OrderProductDao;
import com.no1.book.dao.order.OrderStatusHistoryDao;
import com.no1.book.domain.order.CustomerDto;
import com.no1.book.domain.order.DeliveryAddressDto;
import com.no1.book.domain.order.OrderDto;
import com.no1.book.domain.order.OrderFormDto;
import com.no1.book.domain.order.OrderProductDto;
import com.no1.book.domain.order.OrderStatusHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderStatusHistoryDao orderStatusHistoryDao;

    private final OrderValidator orderValidator = new OrderValidator();

    // 상수 - application.properties에서 정의
    @Value("${MIN_ORDER_AMOUNT_FOR_FREE_DELIVERY}")
    private int minOrderAmountForFreeDelivery;

    @Value("${DELIVERY_FEE}")
    private int deliveryFee;

    /*
    # 주문 화면 진입
    1. 상품상세, 장바구니로부터 받은 주문 정보 저장 (DB 저장 X, 객체에 저장)

    2. 받은 정보로 예상 배송일, 배송비 계산
        - 상품 상세 화면에서 진입한 경우, 배송비를 넘기지 않으므로 계산 필요

    3. 받은 정보로 회원일 경우 배송지, 회원 정보 조회
        - 배송지 정보 : 기본 배송지를 보여주기 위해 필요
        (배송지가 하나라도 추가되었다면 해당 주소가 기본 배송지이며, 기본 배송지는 삭제 불가능하다.)
        - 회원 정보 : 주문 완료 후 배송 table에 수령자 정보를 저장하기 위해 필요
        (비회원 또는 기본 배송지가 없는 회원은 배송지, 수령자 정보를 입력받아야 한다.)

    4. 추가적으로 필요한 정보 입력받기
        - 회원 : 결제수단, 요청 메세지, 공동현관출입 비밀번호
            - 기본 배송지가 없는 경우 + 수령자 및 배송지 정보
        - 비회원 : 결제수단, 요청 메세지, 공동현관출입 비밀번호, 주문 비밀번호, 수령자 정보, 배송지 정보
    */

    /*
    # 주문 버튼 클릭 (=결제 요청)

    1. 주문 정보 검증 - 모든 필수 정보 입력 & 상품 실시간 재고 확인

    2. 결제 요청 및 검증 (결제 성공 가정. 검증은 추후 팀 코드 병합 후 구현)

    3. DB 저장 - 주문, 주문상품, 주문상태, 배송, 결제
        - 다른 table에 주문 번호가 필요하기 때문에 반드시 주문 table에 먼저 저장되어야 한다.
        - 주문 번호는 주문 목록을 최신순으로 정렬하여 가장 첫번째 주문의 주문 번호를 가져오도록 한다.

    4. 주문 완료 페이지로 redirect
        - 뒤로가기를 막기 위해 redirect를 사용하여 주문 완료 페이지에 보내도록 한다. ? 고민 필요
     */

    // 화면 첫 진입시 받은 정보를 OrderFormDto에 저장
    @Override
    public OrderFormDto initOrderInfo(int custId, List<OrderProductDto> productList) {
        OrderFormDto orderFormDto = new OrderFormDto();

        // 1. 회원ID 저장
        // TODO: Q. 회원ID의 경우 다른 페이지에서 진입시 전달받아야 하는가, session에서 가져와야 하는가? -> session으로 하는 게 맞겠다
        //  session에서 받을 경우 비회원 판단은 null로?
        orderFormDto.setCustId(custId);

        // 2. 회원일 경우 회원 table, 배송지 table 조회하여 저장
            // 팀 코드 병합 후 실제 DB에서 받아서 진행 예정
        if(custId > -1) { // 모든 비회원의 custId = -1
            getCustomerInfo(orderFormDto);
            getAddressInfo(orderFormDto);
        }

        // 3. 전달 받은 주문상품 목록 저장
            // 추후 주문 완료 화면으로 data를 보낼 때, 리스트도 주문폼 정보(OrderFormDto 객체)에 담아서 보내기 위함
        orderFormDto.setProductList(productList);

        // 4. 상품 총 금액, 새벽배송 여부, ebook 여부 저장
        setProductInfo(orderFormDto);

        // 5. ebook, 새벽배송에 따른 예상 배송일 저장
        setDeliveryDate(orderFormDto);

        // 6. 상품 금액에 따른 배송비 계산
        setDeliveryPrice(orderFormDto);

        return orderFormDto;
    }

    // 결제 요청
        // 추후 팀 코드 병합 후 요청 필요
    boolean requestPayment() {
        return true;
    }

    // 주문 DB 저장
    @Transactional
    @Override
    public void saveOrder(OrderFormDto orderInfo) {
        // 주문 검증
        orderValidator.validateOrder(orderInfo);

        // regId - 회원일 경우 custId, 비회원일 경우 email 저장
        String regId = orderInfo.getCustId() > -1 ? String.valueOf(orderInfo.getCustId()) : orderInfo.getEmail();

        // 회원 여부
        String custCheck = orderInfo.getCustId() > -1 ? "Y" : "N";

        // 객체 초기화
        OrderDto orderDto = new OrderDto(orderInfo.getCustId(), "RCVD", "301", custCheck, orderInfo.getOrderRequestMessage(), orderInfo.getTotalProdBasePrice(), orderInfo.getTotalDiscPrice(), orderInfo.getDlvPrice(), orderInfo.getTotalSalePrice(), orderInfo.getPwd(), regId, regId);

        try {
            // DB 저장
            orderDao.createOrder(orderDto);
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException("DB에 저장 실패했습니다.");
        }
    }

    // 주문상품 DB 저장
    @Transactional
    @Override
    public void saveOrderProduct(List<OrderProductDto> productList) {
        int ordId = getOrderId();
        String regId = getRegId(ordId);

        try {
            for(OrderProductDto product : productList) {
                // TODO: Q. 시스템 컬럼을 업무에서 사용해도 되는가? & regDate는 반드시 DB에 저장하는 시점으로 저장해야 하는가?
                //  주문상품은 반복문을 돌며 DB에 저장되고, regDate는 저장되는 시점의 "현재 일시"로 저장되어, 같은 주문의 상품이더라도 반복문을 도는 시간에 따라 regDate에 차이가 존재하게 된다.
                //  주문상품의 regDate는 주문상품 취소가능일시를 계산할 때 사용되는데, 시스템 컬럼을 사용하지 말고 업무용으로 사용할 컬럼을 추가해야 하는가?
                product.setOrdId(ordId);
                product.setRegId(regId);
                product.setUpId(regId);

                // DB 저장
                orderProductDao.insertOrderProduct(product);
            }
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException(OrderValidatorErrorMessage.SAVE_DATABASE_FAILED.getMessage());
        }
    }

    // 주문상태이력 DB 저장
    @Transactional
    @Override
    public void saveOrderStatus() {
        int ordId = getOrderId();
        String regId = getRegId(ordId);

        OrderStatusHistoryDto orderStatusHistoryDto = new OrderStatusHistoryDto(ordId, null, "RCVD", "결제 승인", regId, regId);

        try {
            // DB 저장
            orderStatusHistoryDao.createOrderStatusHistory(orderStatusHistoryDto);
        } catch (DataAccessException e) { // DB 접근 중 발생하는 예외
            throw new SystemException("DB에 저장 실패했습니다.");
        }
    }

    // 배송 DB 저장
        // 추후 팀 코드 병합 후 배송 table에 저장 필요
    @Transactional
    @Override
    public void saveDelivery() {
    }

    // 결제 DB 저장
        // 추후 팀 코드 병합 후 결제 table에 저장 필요
    @Transactional
    @Override
    public void savePayment() {
    }

    // 가장 최근의 주문Id 조회
    @Override
    public int getOrderId() {
        // 비회원의 주문 조회를 고려하여, 모든 주문을 조회
        // TODO: 최근 주문 조회시, 동시에 진행되고 있던 다른 고객의 주문 id를 조회할 가능성이 존재하므로, 주문 번호를 미리 생성하여 지정하도록 한다.
        List<OrderDto> orders = orderDao.getAllOrder();
        if(orders.isEmpty()) {
            throw new OrderException("주문이 존재하지 않습니다.");
        }
        return orders.get(0).getOrdId();
    }

    // 특정 주문의 regId 조회
    public String getRegId(int ordId) {
        return orderDao.getOrder(ordId).getRegId();
    }

    // 상품 정보 저장
    // 총 상품 금액, 총 할인 금액 계산
    // 모두 새벽배송, 모두 Ebook 여부 판단

    // TODO: Q. 금액 계산과 새벽 배송 판단은 다른 역할이지만, 둘 모두 판단을 위해서는 반복문이 필요하다.
    //  가독성을 위해 역할에 따라 메서드를 분리해야 하는가, 성능을 위해 반복문 하나만 사용하도록 해야 하는가?
    void setProductInfo(OrderFormDto orderFormDto) {
        int totalProdBasePrice = 0; // 상품 정가 총합
        int totalDiscPrice = 0; // 상품 할인가 총합
        int totalOrderQuantity = 0; // 상품 주문 개수 총합
        boolean isAllDawnDelivery = true; // 모두 새벽배송인가?
        boolean isAllEbook = true; // 모두 Ebook인가?

        for(OrderProductDto product : orderFormDto.getProductList()) {
            totalProdBasePrice += product.getTotalProdPrice();
            totalDiscPrice += product.getTotalDiscPrice();
            totalOrderQuantity += product.getOrdQty();

            if(product.getIsDawnDelivery().equals("N")) {
                isAllDawnDelivery = false;
            }

            if(product.getEbookChk().equals("N")) {
                isAllEbook = false;
            }

            // prodId로 상품 table 조회하여 유효한 상품ID인지 확인
                // 추후 팀 코드 병합 후 구현 필요
            // prodDao.findById(prodId).orElseThrow(() -> new InvalidateProductIdException("유효하지 않은 상품입니다."))
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
        // 총 금액이 15000원보다 적으면 배송비 존재
        int dlvPrice = orderFormDto.getTotalProdBasePrice() - orderFormDto.getTotalDiscPrice() < minOrderAmountForFreeDelivery ? deliveryFee : 0;

        // 총 결제 금액 = 총 상품 금액 - 총 할인 금액 - 배송비
        orderFormDto.setTotalSalePrice(orderFormDto.getTotalProdBasePrice() - orderFormDto.getTotalDiscPrice() - dlvPrice);
        orderFormDto.setDlvPrice(dlvPrice);
    }

    // 회원 정보 조회
        // 추후 팀 코드 병합 후 회원 table에서 조회 필요
    void getCustomerInfo(OrderFormDto orderFormDto) {
        CustomerDto customerDto = new CustomerDto();

        orderFormDto.setEmail(customerDto.getEmail()); // 회원 이메일
        orderFormDto.setName(customerDto.getName()); // 회원 이름
    }

    // 배송 정보 조회
        // 추후 팀 코드 병합 후 배송지 table에서 조회 필요
    void getAddressInfo(OrderFormDto orderFormDto) {
        DeliveryAddressDto deliveryAddressDto = new DeliveryAddressDto();

        orderFormDto.setAddressName(deliveryAddressDto.getName()); // 배송지 이름
        orderFormDto.setTelNum(deliveryAddressDto.getMobileNum()); // 휴대전화번호
        orderFormDto.setZipCode(deliveryAddressDto.getZpcd()); // 우편번호
        orderFormDto.setMainAddress(deliveryAddressDto.getMainAddr()); // 기본주소
        orderFormDto.setDetailAddress(deliveryAddressDto.getDetailAddr()); // 상세주소
        orderFormDto.setDefaultChk(deliveryAddressDto.getDefaultChk()); // 기본배송지 여부
    }

}
