package personal.scraper.web_crawler.scraper;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Web crawler to scrape
 */
public interface Scraper {
    /**
     * Scrape the respective portal against search keyword
     *
     * @param searchTerm
     * @param sampleSize
     * @return List of Jsoup Node Elements containing Search Object
     */
    List<Element> scrape(String searchTerm, int sampleSize);

    /**
     * Extract the used Javascript libraries name & aggregate them by usage
     *
     * @param elements
     * @return Map of JS libraries against their usage count
     */
    Map<String, AtomicInteger> extractAllJsLibrariesByUsage(List<Element> elements);
}
