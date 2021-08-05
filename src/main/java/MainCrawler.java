import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Set;

public class MainCrawler {

    static void printLink(WebElement linkContainer){
        WebElement link = getLink(linkContainer);

        System.out.println(String.format("%s : %s", getLinkText(link), getLinkURL(link)));
    }

    static WebElement getLink(WebElement linkContainer) {
        return linkContainer.findElement(By.cssSelector(":first-child"));
    }

    static String getLinkText(WebElement link){
        return link.findElement(By.cssSelector(".LC20lb.DKV0Md")).getText();
    }

    static String getLinkURL(WebElement link){
        return link.getAttribute("href");
    }

    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.manage().window().maximize();
            webDriver.get("https://google.com");
            WebElement element = webDriver.findElement(By.cssSelector(".gLFyf.gsfi"));

            element.sendKeys("Goku drip");
            element.submit();

            List<WebElement> linkContainers = webDriver.findElements(By.className("yuRUbf"));

            linkContainers.forEach(linkContainer -> printLink(linkContainer));
        }
        finally {
            webDriver.close();
        }
    }
}
