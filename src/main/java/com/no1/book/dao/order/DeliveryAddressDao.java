package com.no1.book.dao.order;

import com.no1.book.domain.order.DeliveryAddressDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeliveryAddressDao {
    // read
    DeliveryAddressDto getDefaultAddress(int custId);

    // update
    void updateDefaultAddress(DeliveryAddressDto deliveryAddressDto);
}
