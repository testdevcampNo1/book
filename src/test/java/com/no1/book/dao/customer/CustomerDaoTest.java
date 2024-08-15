package com.no1.book.dao.customer;

import com.no1.book.domain.customer.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;
    // 회원가입 테스트
    @Test
    public void insertCustomerTest() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustId("혜선");
        customerDto.setPwd("pwd00");
        customerDto.setEmail("email00");
        customerDto.setName("name00");
        customerDto.setMobileNum("010-4912");
        customerDto.setGender("기타");
        customerDto.setBirthDate("2000");
        customerDto.setDate("1999");
        customerDto.setAdultChk("N");
        customerDto.setWithdChk("N");

        customerDao.insertCustomer(customerDto);
        System.out.println("customerDto = " + customerDto.getCustId()+" "+customerDto.getEmail());
    }
    //회원목록 전체조회 테스트
    @Test
    public void selectCustomerListTest() {
        CustomerDto customerDto = new CustomerDto();
        List<CustomerDto> customerDtoList = customerDao.selectCustomerList();
        for (CustomerDto customerDto1 : customerDtoList) {
            System.out.println(formatCustomerDto( customerDto1));
        }
    }@Test
    public void selectCustomerTest() {
        String custId = "1";
        CustomerDto customerDto = customerDao.selectCustomer(custId);

        // Ensure the customer is not null and print their details
        assertNotNull(customerDto, "Customer should not be null");
        System.out.println(formatCustomerDto(customerDto));
    }
    @Test
    public void updateCustomerTest() {
        String custId = "12400";
        CustomerDto customerDto = customerDao.selectCustomer(custId);
        assertNotNull(customerDto, "Customer should not be null before update");

        // Modify the customer's details
        customerDto.setEmail("updated_email@example.com");
        customerDto.setName("Updated Name");
        customerDto.setMobileNum("010-1234-5678");

        // Update the customer in the database

        // Verify that at least one row was affected
        //assertTrue(rowsAffected > 0, "At least one row should be updated");

        // Fetch the updated customer details
        CustomerDto updatedCustomerDto = customerDao.selectCustomer(custId);

        // Verify that the changes were persisted
        assertEquals(customerDto.getEmail(), updatedCustomerDto.getEmail(), "Email should be updated");
        assertEquals(customerDto.getName(), updatedCustomerDto.getName(), "Name should be updated");
        assertEquals(customerDto.getMobileNum(), updatedCustomerDto.getMobileNum(), "Mobile number should be updated");

        // Print the updated customer details
        System.out.println("Updated customer details:");
        System.out.println(formatCustomerDto(updatedCustomerDto));
    }@Test
    public void deleteCustomerTest() {
        String custId = "12400";
        CustomerDto customerDto = customerDao.selectCustomer(custId);
        assertNotNull(customerDto, "Customer should not be null before deletion");

        // Delete the customer from the database
        customerDao.deleteCustomer(custId);

        // Verify that the customer has been deleted
        CustomerDto deletedCustomerDto = customerDao.selectCustomer(custId);
        assertNull(deletedCustomerDto, "Customer should be null after deletion");
    }@Test
    public void idCheckCustomerTest() {
        String existingCustId = "1"; // Assume this ID exists in the database
        String nonExistingCustId = "9999"; // Assume this ID does not exist

        // Check for an existing ID
        int existingCount = customerDao.idCheckCustomer(existingCustId);
        assertTrue(existingCount > 0, "ID should exist in the database");
        System.out.println("\n이미 존재하는 아이디입니다\n");

        // Check for a non-existing ID
        int nonExistingCount = customerDao.idCheckCustomer(nonExistingCustId);
        assertEquals(0, nonExistingCount, "ID should not exist in the database");
        System.out.println("\n사용 가능한 아이디입니다\n");
    }
    @Test
    public void loginCustomerTest() throws Exception{
      CustomerDto customerDto = new CustomerDto();

      customerDto.setCustId("woong174");
      customerDto.setPwd("1q2w3e4r!");

      //customerDto.setCustId("w124352174");
      //customerDto.setPwd("11244r!");
        assertNotNull(customerDto);
        CustomerDto customerDto1 = customerDao.loginCustomer(customerDto.getCustId(),customerDto.getPwd());
        assertEquals(customerDto1.getCustId(),customerDto.getCustId());
        //

    }













    private String formatCustomerDto(CustomerDto customer) {
        return String.format(
                "Customer ID: %s\n" +
                        "Name: %s\n" +
                        "Main Address: %s\n" +
                        "Mobile Number: %s\n" +
                        "Gender: %s\n" +
                        "Email: %s\n" +
                        "Birth Date: %s\n" +
                        "Registration Date: %s\n" +
                        "Registration ID: %s\n" +
                        "Update Date: %s\n" +
                        "Update ID: %s\n" +
                        "Image: %s\n" +
                        "Nickname: %s\n" +
                        "Accumulated Price: %s\n" +
                        "Point: %s\n" +
                        "Terms of Use: %s\n" +
                        "Adult Check: %s\n" +
                        "Withdraw Check: %s\n" +
                        "Grade ID: %s\n" +
                        "------------------------",
                customer.getCustId(),
                customer.getName(),
                customer.getMainAddr(),
                customer.getMobileNum(),
                customer.getGender(),
                customer.getEmail(),
                customer.getBirthDate(),
                customer.getRegDate(),
                customer.getRegId(),
                customer.getUpDate(),
                customer.getUpId(),
                customer.getImage(),
                customer.getNickName(),
                customer.getAccPrice(),
                customer.getPoint(),
                customer.getTou(),
                customer.getAdultChk(),
                customer.getWithdChk(),
                customer.getGradeId()
        );

    }
}