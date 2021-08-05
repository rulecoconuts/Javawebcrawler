package crawler;

public interface GoogleCrawlerParamSet {
    void setParam(String key, Object value);
    void getParam(String key);

    int getDepth();
    void setDepth(int depth);
    String getInitialSearchTerm();
    void setInitialSearchTerm();
}
