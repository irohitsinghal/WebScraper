package personal.scraper.web_crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import personal.scraper.web_crawler.scraper.Scraper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
class ScraperServiceImpl implements ScraperService {

    @Autowired
    private Scraper scraper;

    @Value("${scraper.search-sample-size:50}")
    private int searchSampleSize;

    public List<String> getTopNUsedJavascriptLibraries(String searchTerm, int N) {
        List<Element> results = scraper.scrape(searchTerm, searchSampleSize);
        Map<String, AtomicInteger> libraries = scraper.extractAllJsLibrariesByUsage(results);

        if (libraries == null)
            return new ArrayList<>();

        List list = libraries.entrySet().stream().sorted(Comparator.comparingInt(a -> a.getValue().get()))
                .map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.toList());

        return list.subList(0, Math.min(list.size(), N));
    }
}
