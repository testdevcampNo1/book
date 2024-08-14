/*package com.no1.book.mapper.customer;

import com.no1.book.domain.customer.CustomerDto;
import com.no1.book.mapper.customer.CustomerMapper;
import jakarta.activation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class CustomerDaoImpl implements CustomerMapper {
    @Autowired
    DataSource ds;

    @Override
    public CustomerDto selectCustomer(String id) throws Exception {
        CustomerDto customer = null;
        String sql = "select * from customer where id = ?";

        try(Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, id);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next()) {

            customer = new CustomerDto();
            customer.setId(rs.getString(1));
            customer.setPwd(rs.getString(2));
            customer.setName(rs.getString(3));
            customer.setMain_addr(rs.getString(4));
            customer.setMobile_num(rs.getString(5));
            customer.setGender(rs.getString(6));
            customer.setBirth_date(new String(rs.getDate(7).getTime()));
            customer.setEmail(rs.getString(8));
            }
        }
        return customer;
    }

    @Override
    public int deleteCustomer(String id) throws Exception {
        int rowCnt=0;
        String sql = "delete from customer where id = ?";

        try (Connection conn=ds.getConnection();PreparedStatement pstmt=conn.prepareStatement(sql))
        {
            pstmt.setString(1, id);

            return pstmt.executeUpdate();
        }
    }

    @Override
    public int insertCustomer(CustomerDto customer) throws Exception {
        int rowCnt=0;
        String sql = "insert into customer values(?,?,?,?,?,?)";
        try(Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, customer.getId());
            pstmt.setString(2, customer.getPwd());
            pstmt.setString(3, customer.getName());
            pstmt.setString(4, customer.getMain_addr());
            pstmt.setString(5, customer.getMobile_num());
            pstmt.setString(6, customer.getGender());

            return pstmt.executeUpdate();
        }
    }

    @Override
    public int updateCustomer(CustomerDto customer) throws Exception {
       int rowCnt=0;
        String sql = "UPDATE customer_info " + "SET pwd = ?, name = ? , email = ?, birth_date = ?, sns = ?, reg_date = ? " + "WHERE id = ?";
        try(Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getPwd());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setDate(4, new java.sql.Date(customer.getBirth_date().getTime()));
            pstmt.setString(5, customer.getSns());
            pstmt.setTimestamp(6, new java.sql.Timestamp(customer.getReg_date().getTime()));
            pstmt.setString(7, customer.getId());

            rowCnt = pstmt.executeUpdate();
        }
        return rowCnt;
    }

    @Override
    public int countCustomer() throws Exception {
        String sql = "SELECT count(*) FROM customer_info";

        try(Connection conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery())
        {
            rs.next();
            int result = rs.getInt(1);

            return result;
        }
    }

    @Override
    public void deleteAllCustomer() throws Exception {
        try(Connection conn = ds.getConnection()) {
            String sql = "DELETE FROM user_info";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        }
    }
}
*/