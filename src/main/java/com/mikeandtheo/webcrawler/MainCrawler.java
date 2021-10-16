package com.mikeandtheo.webcrawler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.mikeandtheo.webcrawler.crawler.google.GoogleCrawler;
import com.mikeandtheo.webcrawler.crawler.google.GoogleCrawlerParamSet;
import com.mikeandtheo.webcrawler.crawler.google.SimpleGoogleCrawler;
import com.mikeandtheo.webcrawler.crawler.google.pages.GooglePages;
import com.mikeandtheo.webcrawler.crawler.google.pages.SimpleGoogleCrawlerParams;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;

public class MainCrawler {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
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
