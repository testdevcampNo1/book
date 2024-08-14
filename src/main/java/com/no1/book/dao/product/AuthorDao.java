package com.no1.book.dao.product;

import com.no1.book.domain.product.AuthorDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorDao {

    int count();

    int insert(AuthorDto dto);

    AuthorDto select(String authorId);

    List<AuthorDto> selectAll();

    int update(String authorId);

    int delete(String authorId);

    int deleteAll();

    AuthorDto getAuthorInfo(String prodId);
}
