package com.mikeandtheo.webcrawler.crawler.google.pages;

import java.util.HashMap;
import java.util.Map;
import com.mikeandtheo.webcrawler.crawler.google.GoogleCrawlerParamSet;

public class SimpleGoogleCrawlerParams extends HashMap<String, Object> implements GoogleCrawlerParamSet {
    String initialSearchString;
    int depth;
    final Map<String, Object> map = new HashMap<>();

    @Override
    public void setParam(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object getParam(String key) {
        return map.get(key);
    }

    @Override
    public String getString(String key) {
        return (String)map.get(key);
    }

    @Override
    public void setString(String key, String value) {
        map.put(key, value);
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String getInitialSearchString() {
        return initialSearchString;
    }

    @Override
    public void setInitialSearchString(String initialSearchString) {
        this.initialSearchString = initialSearchString;
    }    
}
