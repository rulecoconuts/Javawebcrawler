package crawler;

public interface WebCrawler<ParameterType, Output> {
    Output crawl(ParameterType parameters);
}
