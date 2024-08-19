package com.no1.book.service.product;

import com.no1.book.domain.product.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthorOrderedByName() throws Exception;

}
