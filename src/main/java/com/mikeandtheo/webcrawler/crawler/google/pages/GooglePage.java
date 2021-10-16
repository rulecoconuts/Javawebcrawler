package com.mikeandtheo.webcrawler.crawler.google.pages;

import java.util.Collection;
import java.util.Map;
import com.mikeandtheo.webcrawler.crawler.Link;

public interface GooglePage<ResultType> {
    void addPageNavLinks(Map<Integer, String> pageNavBarLinks);
    Map<Integer, String> getPageNavLinks();
    void setSearchString(String searchString);
    String getSearchString();
    int getPageNumber();

    Collection<ResultType> getResults();
    void print();
}
