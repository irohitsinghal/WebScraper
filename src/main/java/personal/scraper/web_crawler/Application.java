package personal.scraper.web_crawler;

import personal.scraper.web_crawler.scraper.GoogleScraper;
import personal.scraper.web_crawler.scraper.ScrapeProcessor;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * identify top used Javascript libraries
 */
public class Application {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter search Term: ");
        String searchTerm = in.nextLine();
        in.close();

        ScrapeProcessor processor = new ScrapeProcessor(new GoogleScraper());
        Map<String, AtomicInteger> libraries = processor.processRequest(searchTerm, 100);
    }
}
