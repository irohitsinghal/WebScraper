package personal.scraper.web_crawler.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface ScraperService {

    /**
     * Get Top N Used javascript libraries from Search Results
     *
     * @param searchTerm
     * @param sampleSize
     * @return List of String
     */
    List<String> getTopNUsedJavascriptLibraries(String searchTerm, int N);
}
