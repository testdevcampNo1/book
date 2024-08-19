package com.no1.book.service.product;

import com.no1.book.dao.product.AuthorDao;
import com.no1.book.domain.product.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorDao authorDao;

    @Override
    public List<AuthorDto> getAllAuthorOrderedByName() throws Exception {
        return authorDao.getAllAuthorOrderedByName();
    }
}
