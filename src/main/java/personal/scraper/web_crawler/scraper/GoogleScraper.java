package personal.scraper.web_crawler.scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

public class GoogleScraper implements Scraper {

    private String searchUrl;
    private List<String> searchType;

    public GoogleScraper() {
        this.searchUrl = "https://www.google.com/search?q=%s&num=%s";
        this.searchType = Arrays.asList("Web results", "Twitter results");
    }

    public List<Element> scrape(String searchTerm, int sampleSize) {
        List<Element> results = new ArrayList<>();
        try {
            URL url = new URL(String.format(searchUrl, URLEncoder.encode(searchTerm, CHARSET), sampleSize));

            System.out.println("Searching " + searchTerm + " on Google at url: " + url);
            Document doc = fetchPage(url);
            Elements headers = doc.select("h2");

            for (Element header : headers) {
                if (!searchType.contains(header.text()))
                    continue;
                results.addAll(Arrays.asList(header.nextElementSibling().children().toArray(new Element[]{})));
            }
        } catch (MalformedURLException e) {
            System.out.println("Not able to create google search url with query " + searchTerm + ". Aborting!\n" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot fetch search results currently. Aborting!\n" + e.getMessage());
        }
        return results;
    }

    public Map<String, AtomicInteger> processScrapedData(List<Element> results) {
        Map<String, AtomicInteger> libraries = new ConcurrentHashMap<String, AtomicInteger>();

        results.parallelStream().forEach(result -> {
            String childUrl = null;
            try {
                childUrl = result.select("a[href]").first().absUrl("href");
                extractJS(fetchPage(new URL(childUrl)).head(), libraries);
            } catch (IOException e) {
                System.out.println("Could not fetch page: " + childUrl);
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
                System.out.println("Could not get js file: " + e.getMessage());
            }
        });
    }
}
