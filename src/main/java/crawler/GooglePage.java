package crawler;

import java.util.Collection;

public interface GooglePage<ResultType> {
    int getPageNumber();

    Collection<ResultType> getResults();
}
