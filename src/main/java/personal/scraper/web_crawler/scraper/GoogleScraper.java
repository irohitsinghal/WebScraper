package personal.scraper.web_crawler.scraper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static personal.scraper.web_crawler.utils.WebUtils.CHARSET;
import static personal.scraper.web_crawler.utils.WebUtils.fetchPage;

@Slf4j
@Component
@ConditionalOnProperty(value = "scraper.google.enabled", havingValue = "true")
class GoogleScraper implements Scraper {

    private GoogleScraperProperties properties;

    @Autowired
    public GoogleScraper(GoogleScraperProperties properties) {
        log.info("Using REAL Google Scraper bean");
        this.properties = properties;
    }

    public List<Element> scrape(String searchTerm, int sampleSize) {
        List<Element> results = new ArrayList<>();
        try {
            URL url = new URL(String.format(properties.getSearchUrl(), URLEncoder.encode(searchTerm, CHARSET), sampleSize));

            log.debug("Searching " + searchTerm + " on Google at url: " + url);
            Document doc = fetchPage(url);
            Elements headers = doc.select("h2");

            for (Element header : headers) {
                if (!properties.getSearchType().contains(header.text()))
                    continue;
                results.addAll(Arrays.asList(header.nextElementSibling().children().toArray(new Element[]{})));
            }
        } catch (MalformedURLException e) {
            log.error("Not able to create google search url with query " + searchTerm + ". Aborting!\n" + e.getMessage());
        } catch (IOException e) {
            log.error("Cannot fetch search results currently. Aborting!\n" + e.getMessage());
        }
        return results;
    }

    public Map<String, AtomicInteger> extractAllJsLibrariesByUsage(List<Element> elements) {
        Map<String, AtomicInteger> libraries = new ConcurrentHashMap<String, AtomicInteger>();

        elements.parallelStream().forEach(result -> {
            String childUrl = null;
            try {
                childUrl = result.select("a[href]").first().absUrl("href");
                extractJS(fetchPage(new URL(childUrl)).head(), libraries);
            } catch (IOException e) {
                log.error("Could not fetch page: " + childUrl);
            }
        });

        return libraries;
    }

    private void extractJS(Element head, Map<String, AtomicInteger> libraries) {
        Element scripts[] = head.select("script[src]").toArray(new Element[]{});

        Stream.of(scripts).parallel().forEach(script -> {
            try {
                URL url = new URL(script.absUrl("src"));
                String tempPaths[] = url.getPath().split("/");
                String key = tempPaths[tempPaths.length - 1];

                libraries.compute(key, (k, v) -> {
                    v = (v == null ? new AtomicInteger(0) : v);
                    v.incrementAndGet();
                    return v;
                });
            } catch (MalformedURLException e) {
                log.error("Could not get js file: " + e.getMessage());
            }
        });
    }
}
