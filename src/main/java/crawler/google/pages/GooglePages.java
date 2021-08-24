package crawler.google.pages;

import java.util.Collection;
import java.util.Map;

public interface GooglePages extends Collection<GooglePage> {
    Map<Integer, String> getPageNavLinks();
}
