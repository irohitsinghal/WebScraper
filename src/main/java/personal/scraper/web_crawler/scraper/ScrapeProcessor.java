package personal.scraper.web_crawler.scraper;

import org.jsoup.nodes.Element;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ScrapeProcessor {
    private Scraper scraper;

    public ScrapeProcessor(Scraper scraper) {
        this.scraper = scraper;
    }

    public Map<String, AtomicInteger> processRequest(String searchTerm, int sampleSize) {
        List<Element> results = scraper.scrape(searchTerm, sampleSize);

        Map<String, AtomicInteger> libraries = scraper.processScrapedData(results);

        System.out.println("\nTop 5 most used Javascript libraries:");
        printTopFiveJsLibraries(libraries);

        return libraries;
    }

    private void printTopFiveJsLibraries(Map<String, AtomicInteger> libraries) {
        List list = libraries.entrySet().stream().sorted(Comparator.comparingInt(a -> a.getValue().get()))
                .map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.toList());

        list.subList(0, Math.min(list.size(), 5)).stream().forEach(System.out::println);
    }
}
