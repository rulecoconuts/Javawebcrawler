package crawler.google.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import crawler.Link;

public class SimpleGooglePage implements GooglePage<Link> {
    int pageNumber;

    String searchString;

    Link link;
    final Map<Integer, String> pageNavLinks = new HashMap<>();

    final List<Link> contents = new ArrayList<>();

    public SimpleGooglePage(int pageNumber, List<Link> contents) {
        this.pageNumber = pageNumber;
        this.contents.addAll(contents);
    }

    public SimpleGooglePage(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SimpleGooglePage(int pageNumber, Link link) {
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

    public static SimpleGooglePage from(int pageNumber, String searchString, Link link, Collection<Link> results) {
        SimpleGooglePage page = new SimpleGooglePage(pageNumber);
        page.contents.addAll(results);
        page.searchString = searchString;
        page.link = link;

        return page;
    }

    /**
     * Get page link
     * 
     * @return
     */
    Link getLink() {
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
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Page #%d\n\n", pageNumber));
        builder.append(String.format("Searchstring: %s\n\n", searchString));
        if (link != null)
            builder.append(String.format("Page link: %s\n\n", link.getUrl()));
        builder.append("Results:\n");

        for (Link link : contents) {
            builder.append(link.toString());
            builder.append("\n\n");
        }
        return builder.toString();
    }

    /**
     * Print page contents
     */
    @Override
    public void print() {
        System.out.println(toString());
    }

    @Override
    public void addPageNavLinks(Map<Integer, String> pageNavBarLinks) {
        pageNavLinks.putAll(pageNavBarLinks);       
    }

    @Override
    public Map<Integer, String> getPageNavLinks() {
        return pageNavLinks;
    }
}
