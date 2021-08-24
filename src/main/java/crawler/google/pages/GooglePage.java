package crawler.google.pages;

import java.util.Collection;
import crawler.Link;

public interface GooglePage<ResultType> {
    void setSearchString(String searchString);
    String getSearchString();
    int getPageNumber();

    Collection<ResultType> getResults();
    void print();
}
