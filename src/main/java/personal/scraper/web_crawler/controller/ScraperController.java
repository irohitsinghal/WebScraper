package personal.scraper.web_crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import personal.scraper.web_crawler.service.ScraperService;

import java.util.List;

@RestController
@RequestMapping("api/v1/js")
public class ScraperController {

    @Autowired
    private ScraperService scraperService;

    @GetMapping
    List<String> getTopUsedLibraries(@RequestParam String keyword, @RequestParam int size) {
        return scraperService.getTopNUsedJavascriptLibraries(keyword, size);
    }
}
