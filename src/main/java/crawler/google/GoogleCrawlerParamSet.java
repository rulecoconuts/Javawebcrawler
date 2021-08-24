package crawler.google;

public interface GoogleCrawlerParamSet {
    void setParam(String key, Object value);
    Object getParam(String key);
    String getString(String key);
    void setString(String key, String value);
    int getDepth();
    void setDepth(int depth);
    String getInitialSearchString();
    void setInitialSearchString(String initialSearchTerm);
}
