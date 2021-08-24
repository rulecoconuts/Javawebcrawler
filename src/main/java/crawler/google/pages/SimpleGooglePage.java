package crawler.google.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import crawler.Link;

public class SimpleGooglePage implements GooglePage<Link>{
    int pageNumber;

    String searchString;

    Link link;

    final List<Link> contents = new ArrayList<>();
    
    public SimpleGooglePage(int pageNumber, List<Link> contents){
        this.pageNumber = pageNumber;
        this.contents.addAll(contents);
    }

    public SimpleGooglePage(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public SimpleGooglePage(int pageNumber, Link link){
        this(pageNumber);
        this.link = link;
    }

    @Override
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    public Collection<Link> getResults() {
        return contents;
    }

    public static SimpleGooglePage from(int pageNumber, Collection<Link> results){
        SimpleGooglePage page = new SimpleGooglePage(pageNumber);
        page.contents.addAll(results);

        return page;
    }

    /**
     * Get page link
     * @return
     */
    Link getLink(){
        return link;
    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public String getSearchString() {
        return searchString;
    }

    @Override
    public void print() {
        StringBuilder builder = new StringBuilder();
        System.out.println(builder.toString());
    }
}
