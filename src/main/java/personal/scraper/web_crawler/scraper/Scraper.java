package personal.scraper.web_crawler.scraper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/** 
 * Web crawler to scrape google results
 */
public interface Scraper {
	Document fetchPage(URL url) throws IOException;

	List<Element> scrapeGoogle(String searchTerm);
}
