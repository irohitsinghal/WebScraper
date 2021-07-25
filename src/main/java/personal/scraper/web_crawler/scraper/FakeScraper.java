package personal.scraper.web_crawler.scraper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ConditionalOnProperty(value = "scraper.google.enabled", havingValue = "false")
public class FakeScraper implements Scraper {

    public FakeScraper() {
        log.info("Using FAKE Scraper bean");
    }

    @Override
    public List<Element> scrape(String searchTerm, int sampleSize) {
        log.info("No action taken on scrape call");
        return null;
    }

    @Override
    public Map<String, AtomicInteger> extractAllJsLibrariesByUsage(List<Element> elements) {
        log.info("No action taken on extractAllJsLibraries call");
        return null;
    }
}
