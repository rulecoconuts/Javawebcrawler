package com.mikeandtheo.webcrawler.crawler;

public interface WebCrawler<ParameterType, Output> {
    Output crawl(ParameterType parameters);
}
