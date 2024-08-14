package com.no1.book.dao.customer;

import com.no1.book.domain.customer.CustomerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerDao {

    public int insertCustomer(CustomerDto customerDto);

    public List<CustomerDto> selectCustomerList();

    public CustomerDto selectCustomer(String custId);

    public CustomerDto loginCustomer(String custId, String pwd);


    public int idCheckCustomer(String custId);


    public void updateCustomer(CustomerDto customerDto);

    public void deleteCustomer(String custId);


}

   /*
    List<CustomerDto> list();
    public List<CustomerDto> getCustomerList();
    int insert(String name);
    int delete(String id);
    int update(String name);
    void deleteAllCustomer() throws Exception;

     */

