package com.web.spirder.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author hezifeng
 * @create 2023/3/27 14:40
 */
public interface MovieSpiderService {

    String getDownloadUrl(String url, Map<String, String> cookieMap) throws IOException;

    Map<String, String> getMovieLinks(String url, Map<String, String> cookieMap) throws IOException;

    void searchAll(String keyword, Map<String, String> cookieMap) throws Exception;

    Integer search(String keyword, Map<String, String> cookieMap, Integer pageNumber) throws Exception;
}
