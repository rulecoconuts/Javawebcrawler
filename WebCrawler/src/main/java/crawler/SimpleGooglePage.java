package crawler;

import java.util.Collection;

public class SimpleGooglePage implements GooglePage<Link>{
    int pageNumber;

    public SimpleGooglePage(int pageNumber){
        this.pageNumber = pageNumber;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public Collection<Link> getResults() {
        return null;
    }
}
