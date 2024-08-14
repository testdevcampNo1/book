package com.no1.book.service.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.dao.customer.CustomerDao;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("local")
public class CustomerServiceTest {
    @Resource
    CustomerService customerService;

    @Resource
    CustomerDao customerDao;


    /*@Test
    void testCreateCustomer() {
        // given
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustId("her03");
        customerDto.setName("John Doe");
        customerDto.setEmail("john.doe@example.com");

        // when
        customerService.signUp(customerDto);

        // then
        assertThat(customerDto.getCustId()).isEqualTo("her03");
        assertThat(customerDto.getName()).isEqualTo("John Doe");
        assertThat(customerDto.getEmail()).isEqualTo("john.doe@example.com");
    }*/
    @Transactional@Rollback(false)
    @DisplayName("signUp 회원가입")
    @Test
    public void testsignUp() {

        //Given
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustId("12600");
        customerDto.setPwd("pwd01");
        customerDto.setEmail("email01");
        customerDto.setName("name01");
        customerDto.setMobileNum("010-4913");
        customerDto.setGender("남성");
        customerDto.setBirthDate("1990");
        customerDto.setDate("1990");
        customerDto.setAdultChk("Y");
        customerDto.setWithdChk("N");
        // Set other fields...
       CustomerDto testCustomer =customerService.signUp(customerDto);

        assertNotNull(customerDto);

        // Then
        assertEquals(customerDto.getCustId(), testCustomer.getCustId());
        assertEquals(customerDto.getEmail(), testCustomer.getEmail());
        assertEquals(customerDto.getMobileNum(), testCustomer.getMobileNum());
    }
    @Test
    //@Transactional
    @DisplayName("로그인 시도")
    public void testLogin() {
        // Given
        String custId = "woong174";
        String pwd = "1q2w3e4r!!";
        CustomerDto existingCustomer = new CustomerDto();
        existingCustomer.setCustId(custId);
        existingCustomer.setPwd(pwd);
        existingCustomer.setName("강영웅");

        // Stubbing the DAO method

        CustomerDto result = customerService.login(custId, pwd);

        // Then
        assertNotNull(result, "로그인 실패: 사용자 정보가 반환되지 않았습니다.");
        assertEquals(custId, result.getCustId(), "로그인 실패: 사용자 ID가 일치하지 않습니다.");
        assertEquals("강영웅", result.getName(), "로그인 실패: 사용자 이름이 일치하지 않습니다.");
    }

    @DisplayName("목록 검색")
    @Test
    public void testGetCustomerList() {
        // When
        List<CustomerDto> customerList = customerService.getCustomerList();

        // Then
        assertNotNull(customerList);
        assertTrue(customerList.size() > 0);  // Ensure the list is not empty
        // Optionally, assert specific properties of the customers
        CustomerDto firstCustomer = customerList.get(0);
        assertNotNull(firstCustomer.getCustId());
        assertNotNull(firstCustomer.getName());
    }
    @Test
    public void testEditCustomerInfo() {
        // Given
        String custId = "12600";
        CustomerDto originalCustomer = customerService.getCustomerById(custId);
        assertNotNull(originalCustomer);

        // Create a new CustomerDto with updated information
        CustomerDto updatedCustomer = new CustomerDto();
        updatedCustomer.setCustId(custId);
        updatedCustomer.setName("Updated Name");
        updatedCustomer.setEmail("updated.email@example.com");
        // Update other necessary fields

        // When
        customerService.editInfo(custId);

        // Then
        CustomerDto modifiedCustomer = customerService.getCustomerById(custId);
        assertEquals("Updated Name", modifiedCustomer.getName());
        assertEquals("updated.email@example.com", modifiedCustomer.getEmail());
    }
}

/*@MockBean
    private CustomerDao customerDao;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        // No need for MockitoAnnotations.openMocks(this) as @MockBean and Spring context will handle it
    }*/