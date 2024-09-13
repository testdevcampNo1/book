package com.no1.book.service.product;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public interface APIService {

    public String translateText(String textToTranslate) throws IOException;
}
