package com.mikeandtheo.webcrawler.crawler.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.mikeandtheo.webcrawler.crawler.Link;
import com.mikeandtheo.webcrawler.crawler.google.pages.GooglePage;
import com.mikeandtheo.webcrawler.crawler.google.pages.GooglePages;
import com.mikeandtheo.webcrawler.crawler.google.pages.SimpleGooglePage;
import com.mikeandtheo.webcrawler.crawler.google.pages.SimpleGooglePages;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SimpleGoogleCrawler implements GoogleCrawler {
    final WebDriver webDriver;

    public SimpleGoogleCrawler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Get page navigation bar
     * @param webDriver
     * @return
     */
    WebElement getPageNavBar(WebDriver webDriver) {
        return webDriver.findElement(By.className("AaVjTc"));
    }

    /**
     * Get the anchor element that links to a page.
     * 
     * Returns null if the anchor element cannot be found in the page nav bar
     * 
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    WebElement getLinkToPage(int pageNumber, WebElement pageNavBar) {
        String selector = String.format("a[aria-label=\'Page %d\']", pageNumber);
        List<WebElement> results = pageNavBar.findElements(By.cssSelector(selector));
        return results.size() > 0 ? results.get(0) : null;
    }

    /**
     * Get next page navigation bar.
     * 
     * Google search page navigators are divided into buckets, each consisting of 10 pages.
     * 
     * This method gets the next bucket. It does not just go the next page
     * 
     * @param pageNavBar
     * @return
     */
    WebElement getNextPageNavBar(WebElement pageNavBar) {
        WebElement nextButtonContainer =
                pageNavBar.findElement(By.cssSelector("td[aria-level='3']"));
        WebElement lastPageContainer =
                nextButtonContainer.findElement(By.xpath("preceding-sibling::*[1]"));

        lastPageContainer.findElement(By.tagName("a")).click();

        return getPageNavBar(webDriver);
    }

    /**
     * Get navbar links
     * 
     * @param pageNavBar
     * @return
     */
    Map<Integer, String> getPageNavLinks(WebElement pageNavBar) {
        Map<Integer, String> links = new HashMap<>();
        Integer currentPageNo = getCurrentPageNo(pageNavBar);
        links.put(currentPageNo, webDriver.getCurrentUrl());

        List<WebElement> linkContainers =
                pageNavBar.findElements(By.cssSelector("a[aria-label*='Page']"));

        for (WebElement linkContainer : linkContainers) {
            WebElement anchorElement = linkContainer;
            int pageNumber = Integer.parseInt(anchorElement.getText());

            if (pageNumber > currentPageNo)
                links.put(pageNumber, anchorElement.getAttribute("href"));
        }

        return links;
    }

    /**
     * Get current page no of search result page
     * @param pageNavBar
     * @return
     */
    Integer getCurrentPageNo(WebElement pageNavBar) {
        WebElement currentPage = pageNavBar.findElement(By.className("YyVfkd"));
        return Integer.parseInt(currentPage.getText());
    }

    /**
     * Check if the browser is on the specified page number of google results
     * 
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    boolean isOnPageNumber(int pageNumber, WebElement pageNavBar) {
        return getCurrentPageNo(pageNavBar).equals(pageNumber);
    }

    /**
     * Go to last page
     * 
     * @param pageNavLinks
     */
    void goToLastPage(Map<Integer, String> pageNavLinks) {
        int lastPageNo = 0;
        for (Integer pageNo : pageNavLinks.keySet()) {
            if (pageNo > lastPageNo)
                lastPageNo = pageNo;
        }

        webDriver.get(pageNavLinks.get(lastPageNo));
    }

    /**
     * Go to the specified page
     * 
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    Map<Integer, String> goToPage(int pageNumber, Map<Integer, String> pageNavLinks) {
        Map<Integer, String> currentPageNavLinks = pageNavLinks;
        if (currentPageNavLinks.containsKey(pageNumber)) {
            // Go to page if we previously found it
            webDriver.get(currentPageNavLinks.get(pageNumber));
            return currentPageNavLinks;
        }

        String linkToPage = null;

        while (linkToPage == null) {
            // Get current page nav bar
            WebElement curPageNavBar = getPageNavBar(webDriver);
            Map<Integer, String> nextPageNavLinks = getPageNavLinks(curPageNavBar);

            // There are no pages left if the next Page nav links are a subset of
            // the current page nav links
            if (currentPageNavLinks.keySet().containsAll(nextPageNavLinks.keySet()))
                return null;
            currentPageNavLinks.clear();
            currentPageNavLinks.putAll(nextPageNavLinks);

            // If we have found the page go to it 
            // else go to last page available
            if (currentPageNavLinks.containsKey(pageNumber))
                linkToPage = currentPageNavLinks.get(pageNumber);
            else
                goToLastPage(currentPageNavLinks);
        }

        webDriver.get(linkToPage);

        return currentPageNavLinks;
    }

    /**
     * Exclude page navigation links from the links gotten from a page
     * @param pageLinks
     * @param pageNavLinks
     */
    void removePageNavLinks(Set<Link> pageLinks, Map<Integer, String> pageNavLinks){
        Set<Link> pageNavRep = new HashSet<>();
        for(Entry<Integer, String> linkEntry : pageNavLinks.entrySet()){
            pageNavRep.add(new Link(linkEntry.getKey().toString(), linkEntry.getValue()));
        }

        pageLinks.removeAll(pageNavRep);
    }

    /**
     * Record the results of a page
     * 
     * @param pageNumber Number of the page
     * @param pages GooglePages object to store the results in
     * @param pageNavigationBar Page navigation bar on the current page
     * @return
     */
    GooglePage getPage(int pageNumber, String searchString, Map<Integer, String> pageNavLinks) {
        Map<Integer, String> currentPageNavLinks = goToPage(pageNumber, pageNavLinks);
        
        if(currentPageNavLinks == null) return null;
        Set<Link> pageLinks = getPageLinks(webDriver);
        Link linkToPage = new Link("", webDriver.getCurrentUrl());
        GooglePage<Link> page = SimpleGooglePage.from(pageNumber, searchString,
                linkToPage, pageLinks);
                
        page.addPageNavLinks(currentPageNavLinks);
        removePageNavLinks(pageLinks, pageNavLinks);

        return page;
    }

    /**
     * Perform a search on google
     * 
     * @param searchString
     * @param webDriver
     */
    void search(String searchString, WebDriver webDriver) {
        webDriver.get("https://google.com");
        WebElement element = webDriver.findElement(By.cssSelector(".gLFyf.gsfi"));
        element.sendKeys(searchString);
        element.submit();
    }

    /**
     * Traverse the search results of the current page
     * @param searchTerm
     * @param pageNos
     * @return Pages of the search results as GooglePages
     */
    GooglePages traverseSearchResults(String searchTerm, List<Integer> pageNos){
        SimpleGooglePages pages = new SimpleGooglePages();
        Map<Integer, String> pageNavLinks = new HashMap<>();

        // Traverse search result pages
        for (Integer pageNumber : pageNos) {
            GooglePage page = getPage(pageNumber, searchTerm, pageNavLinks);
            if (page == null)
                break;
            pages.add(page);
        }
        return pages;
    }

    /**
     * Crawl through google search results
     * 
     * @param parameters Parameters to control the crawl
     */
    @Override
    public GooglePages crawl(GoogleCrawlerParamSet parameters) {
        // Prepare params
        String searchTerm = parameters.getInitialSearchString();
        List<Integer> pageNos = (List<Integer>) parameters.getParam("pages");
        pageNos.sort(Comparator.naturalOrder());

        search(searchTerm, webDriver);

        return traverseSearchResults(searchTerm, pageNos);
    }

    /**
     * Get the results on a google search page
     * 
     * @param webDriver
     * @return
     */
    Set<Link> getPageLinks(WebDriver webDriver) {
        Set<Link> links = new HashSet<>();
        List<WebElement> anchorLinks = webDriver.findElements(By.tagName("a"));

        for (WebElement anchorLink : anchorLinks)
            links.add(new Link(getLinkTitle(anchorLink), getLinkURL(anchorLink)));
        return links;
    }

    /**
     * Get search result elements
     * 
     * @param webDriver
     * @return
     */
    public List<WebElement> getSearchResultElements(WebDriver webDriver) {
        return webDriver.findElements(By.className("yuRUbf"));
    }

    /**
     * Get anchor tag in search result element
     * 
     * @param resultElement
     * @return
     */
    public WebElement getLink(WebElement resultElement) {
        return resultElement.findElement(By.cssSelector(":first-child"));
    }

    /**
     * Get anchor tag text
     * 
     * @param link
     * @return
     */
    public String getLinkText(WebElement link) {
        return link.findElement(By.cssSelector(".LC20lb.DKV0Md")).getText();
    }

    /**
     * Get anchor tag HREF/URL
     * 
     * @param link
     * @return
     */
    public String getLinkURL(WebElement link) {
        return link.getAttribute("href");
    }

    /**
     * Get the text inside an anchor tag
     * @param anchorLink
     * @return
     */
    public String getLinkTitle(WebElement anchorLink){
        return anchorLink.getText();
    }
}
