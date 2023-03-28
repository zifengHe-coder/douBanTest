package com.web.spirder.demo.service.impl;

import com.web.spirder.demo.service.MovieSpiderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author hezifeng
 * @create 2023/3/27 14:41
 */
@Service
public class MovieSpiderServiceImpl implements MovieSpiderService {
    private static Logger logger = LoggerFactory.getLogger(MovieSpiderServiceImpl.class);

    private Map<String, String> showMap = new LinkedHashMap<>();
    private Map<String, String> movieMap = new LinkedHashMap<>();

    private static List<String> filterUrls = Arrays.asList("/movies","/movies/3d","/movies/4k"
            ,"/movies/dolby-vision","/shows","/shows/4k","/shows/dolby-vision",
            "/movies","/shows");

    @Override
    public String getDownloadUrl(String url, Map<String, String> cookieMap) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0");
        Document loggedInDoc = Jsoup.connect(url)
                .headers(headers)
                .cookies(cookieMap)
                .get();
        Elements links = loggedInDoc.select("a[href]");
        for (Element link : links) {
            if (link.hasAttr("href") && "点击使用磁力下载".equals(link.text())) {
                return link.attr("href");
            } else if (link.hasAttr("href") && "百度网盘下载".equals(link.text())) {
                String pickCode = "";
                Elements textElements = loggedInDoc.getElementsContainingText("提取码");
                for (Element textElement : textElements) {
                    if (textElement.text().contains("提取码")) {
                        pickCode = textElement.text();
                    }
                }
                return link.attr("href") + ";" + pickCode;
            } else if (link.hasAttr("href") && "阿里云盘下载".equals(link.text())) {
                return link.attr("href");
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getMovieLinks(String url, Map<String, String> cookieMap) throws IOException {
        Map<String, String> torrentMap = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0");
        String loggedInUrl = url;
        Document loggedInDoc = Jsoup.connect(loggedInUrl)
                .headers(headers)
                .cookies(cookieMap)
                .get();
        Elements links = loggedInDoc.select("a[href]");
        for (Element link : links) {
            if (link.hasAttr("title") && link.hasAttr("href") && StringUtils.isNotBlank(link.attr("title"))) {
                torrentMap.put(link.attr("title"), "https://www.mini4k.com" + link.attr("href"));
            }
        }
        return torrentMap;
    }

    @Override
    public void searchAll(String keyword, Map<String, String> cookieMap) throws Exception {
        for (int pageNum = 0; search(keyword, cookieMap, pageNum)!= null; pageNum++) {
        }
        if (showMap.size() > 0) {
            for (Map.Entry<String, String> entry : showMap.entrySet()) {
                String title = entry.getKey();
                String url = entry.getValue();
                logger.info("电视剧 {} 链接 {} ", title, url);
                Map<String, String> torrentMap = getMovieLinks(url, cookieMap);
                if (torrentMap.size() > 0) {
                    logger.info(">>>>>>>>>>>>>>>>>> {} 链接 >>>>>>>>>>>>>>>>>>>>>", title);
                    for (Map.Entry<String, String> torrent : torrentMap.entrySet()) {
                        String magnet = getDownloadUrl(torrent.getValue(), cookieMap);
                        logger.info("{} >> url {} , download {}", torrent.getKey(), torrent.getValue(), magnet);
                    }
                    logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                }
            }
        }
        if (movieMap.size() > 0) {
            for (Map.Entry<String, String> entry : movieMap.entrySet()) {
                String title = entry.getKey();
                String url = entry.getValue();
                logger.info("电影 {} 链接 {} ", title, url);
            }
        }
    }

    @Override
    public Integer search(String keyword, Map<String, String> cookieMap, Integer pageNumber) throws Exception {
        Integer pageNum = pageNumber;
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0");
        String url = "https://www.mini4k.com/search?term=" + URLEncoder.encode(keyword,"UTF-8");
        if (pageNumber > 0) {
            url += "&page="+pageNumber;
        }
//        logger.info("访问url :{}" ,url);
        Document loggedInDoc = Jsoup.connect(url)
                .headers(headers)
                .cookies(cookieMap)
                .get();
        Elements links = loggedInDoc.select("a[href]");
        for (Element link : links) {
            // shows为电视剧 movies为电影
            if (filterUrls.contains(link.attr("href"))) continue;
            if ((link.attr("href").startsWith("/shows") || link.attr("href").startsWith("/movies")) && StringUtils.isNotBlank(link.text())) {
                if (link.attr("href").startsWith("/shows")) {
                    showMap.put(link.text(), "https://www.mini4k.com" + link.attr("href"));
                } else {
                    movieMap.put(link.text(), "https://www.mini4k.com" + link.attr("href"));
                }
            } else if (link.attr("href").contains(URLEncoder.encode(keyword,"UTF-8"))) {
                String pageNumStr = link.attr("href").substring(link.attr("href").lastIndexOf("=") + 1);
                if (StringUtils.isNotBlank(pageNumStr)) {
                    if (Integer.valueOf(pageNumStr) > pageNum) return Integer.valueOf(pageNumStr);
                }
            }
        }
        return null;
    }
}
