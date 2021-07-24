package personal.scraper.web_crawler.scraper;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Web crawler to scrape google results
 */
public interface Scraper {
    List<Element> scrape(String searchTerm, int sampleSize);
    Map<String, AtomicInteger> processScrapedData(List<Element> results);
}
