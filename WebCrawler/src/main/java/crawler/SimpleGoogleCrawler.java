package crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleGoogleCrawler implements GoogleCrawler {

    @Override
    public GooglePage crawl(GoogleCrawlerParamSet parameters) {
        String seachTerm = parameters.getInitialSearchTerm();


        return null;
    }

    Set<Link> getPageResults(WebDriver webDriver){
        List<WebElement> linkContainers = getLinkContainers(webDriver);
        return linkContainers.stream().map(linkContainer->getLink(linkContainer))
                .map(link-> new Link(getLinkText(link), getLinkURL(link)))
                .collect(Collectors.toSet());
    }

    public List<WebElement> getLinkContainers(WebDriver webDriver){
        return webDriver.findElements(By.className("yuRUbf"));
    }

    public WebElement getLink(WebElement linkContainer) {
        return linkContainer.findElement(By.cssSelector(":first-child"));
    }

    public String getLinkText(WebElement link) {
        return link.findElement(By.cssSelector(".LC20lb.DKV0Md")).getText();
    }

    public String getLinkURL(WebElement link) {
        return link.getAttribute("href");
    }
}
