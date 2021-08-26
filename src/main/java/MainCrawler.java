import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import crawler.google.GoogleCrawler;
import crawler.google.GoogleCrawlerParamSet;
import crawler.google.SimpleGoogleCrawler;
import crawler.google.pages.GooglePages;
import crawler.google.pages.SimpleGoogleCrawlerParams;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class MainCrawler {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "drivers/92.0.4515.107/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        try {
            GoogleCrawler googleCrawler = new SimpleGoogleCrawler(webDriver);
            List<Integer> pagesToSearch = new ArrayList<>();
            for (int page = 1; page <= 20; page++)
                pagesToSearch.add(page);

            GoogleCrawlerParamSet googleCrawlerParamSet = new SimpleGoogleCrawlerParams();
            googleCrawlerParamSet.setParam("pages", pagesToSearch);
            googleCrawlerParamSet.setParam("waitTimeBetweenRequests", 3000);
            googleCrawlerParamSet.setInitialSearchString("AOT");
            GooglePages pages = googleCrawler.crawl(googleCrawlerParamSet);
            System.out.println(pages.toString());
        } finally {
            webDriver.close();
        }
    }
}
