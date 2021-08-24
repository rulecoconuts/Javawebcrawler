package crawler.google;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import crawler.Link;
import crawler.google.pages.GooglePage;
import crawler.google.pages.GooglePages;
import crawler.google.pages.SimpleGooglePage;
import crawler.google.pages.SimpleGooglePages;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleGoogleCrawler implements GoogleCrawler {
    final WebDriver webDriver;

    public SimpleGoogleCrawler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    WebElement getPageNavBar(WebDriver webDriver) {
        return webDriver.findElement(By.className(".AaVjTc"));
    }

    /**
     * Get the anchor element that links to a page.
     * 
     * Returns null if the anchor element cannot be found in the page nav bar
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    WebElement getLinkToPage(int pageNumber, WebElement pageNavBar) {
        String selector = String.format("a[aria-label=\'Page %d\']", pageNumber);
        List<WebElement> results = pageNavBar.findElements(By.cssSelector(selector));
        return results.size() > 0? results.get(0) : null;
    }

    /**
     * Get next page navigation bar.
     * 
     * Google search page navigators are divided into buckets, each consisting of 10 pages.
     * 
     * This method gets the next bucket. It does not just go the next page
     * @param pageNavBar
     * @return
     */
    WebElement getNextPageNavBar(WebElement pageNavBar) {
        WebElement nextButtonContainer = pageNavBar.findElement(By.cssSelector("td[aria-level='3']"));
        WebElement lastPageContainer = nextButtonContainer.findElement(By.xpath("preceding-sibling::*[1]"));

        lastPageContainer.findElement(By.tagName("a")).click();

        return getPageNavBar(webDriver);
    }

    /**
     * Checks whether the browser is on the specified page number of google results
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    boolean isOnPageNumber(int pageNumber, WebElement pageNavBar) {
        WebElement currentPage = pageNavBar.findElement(By.className(".YyVfkd"));
        return currentPage.getText().equals(Integer.valueOf(pageNumber).toString());
    }

    /**
     * Go to the specified page
     * @param pageNumber
     * @param pageNavBar
     * @return
     */
    WebElement goToPage(int pageNumber, WebElement pageNavBar) {
        WebElement curPageNavBar = pageNavBar;

        // If page navigators are empty get page navigators on the current page
        if (curPageNavBar == null)
            curPageNavBar = getPageNavBar(webDriver);

        if (isOnPageNumber(pageNumber, curPageNavBar))
            return curPageNavBar;

        WebElement linkToPage = null;

        while (linkToPage == null) {
            // Search for link to page in the navigator list
            linkToPage = getLinkToPage(pageNumber, curPageNavBar);

            if (linkToPage == null)
                // If page was not found get the navigation bar of the next set of pages
                curPageNavBar = getNextPageNavBar(curPageNavBar);
        }

        linkToPage.click();

        // record page results
        return curPageNavBar;
    }

    /**
     * Record the results of a page
     * 
     * @param pageNumber Number of the page
     * @param pages GooglePages object to store the results in
     * @param pageNavigationBar Page navigation bar on the current page
     * @return
     */
    WebElement getPage(int pageNumber, GooglePages pages, WebElement pageNavigationBar) {
        WebElement curPageNavBar = goToPage(pageNumber, pageNavigationBar);
        pages.add(SimpleGooglePage.from(pageNumber, getPageLinks(webDriver)));
        return curPageNavBar;
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
        element.sendKeys("Goku drip");
        element.submit();
    }

    /**
     * Crawl through google search results
     * 
     * @param parameters Parameters to control the crawl
     */
    @Override
    public GooglePages crawl(GoogleCrawlerParamSet parameters) {
        SimpleGooglePages pages = new SimpleGooglePages();
        WebElement pageNavigationBar = null;

        String searchTerm = parameters.getInitialSearchString();
        List<Integer> pageNos = (List<Integer>) parameters.getParam("pages");
        pageNos.sort(Comparator.naturalOrder());

        search(searchTerm, webDriver);

        for (Integer pageNumber : pageNos)
            pageNavigationBar = getPage(pageNumber, pages, pageNavigationBar);
        return pages;
    }

    /**
     * Get the results on a google search page
     * 
     * @param webDriver
     * @return
     */
    Set<Link> getPageLinks(WebDriver webDriver) {
        List<WebElement> linkContainers = getSearchResultElements(webDriver);

        return linkContainers.stream().map(linkContainer -> getLink(linkContainer))
                .map(link -> new Link(getLinkText(link), getLinkURL(link)))
                .collect(Collectors.toSet());
    }

    /**
     * Get search result elements
     * @param webDriver
     * @return
     */
    public List<WebElement> getSearchResultElements(WebDriver webDriver) {
        return webDriver.findElements(By.className("yuRUbf"));
    }

    /**
     * Get anchor tag in search result element
     * @param resultElement
     * @return
     */
    public WebElement getLink(WebElement resultElement) {
        return resultElement.findElement(By.cssSelector(":first-child"));
    }

    /**
     * Get anchor tag text
     * @param link
     * @return
     */
    public String getLinkText(WebElement link) {
        return link.findElement(By.cssSelector(".LC20lb.DKV0Md")).getText();
    }

    /**
     * Get anchor tag HREF/URL
     * @param link
     * @return
     */
    public String getLinkURL(WebElement link) {
        return link.getAttribute("href");
    }
}
