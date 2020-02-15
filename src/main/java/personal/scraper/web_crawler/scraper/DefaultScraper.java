package personal.scraper.web_crawler.scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DefaultScraper implements Scraper {

	private String google;
	private List<String> searchType;
	private int noOfResults;
	private String CHARSET;
	
	public DefaultScraper(int noOfResults) {
		this.noOfResults = noOfResults;
		this.google = "https://www.google.com/search?q=%s&num=%s";
		this.searchType = Arrays.asList("Web results", "Twitter results");
		this.CHARSET = "UTF-8";
	}

	public Document fetchPage(URL url) throws IOException {
		return Jsoup.connect(url.toString()).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
				.header("Content-Language", "en-US").get();
	}

	public List<Element> scrapeGoogle(String searchTerm) {
		List<Element> results = new ArrayList<>();
		try {
			URL url = new URL(String.format(google, URLEncoder.encode(searchTerm, CHARSET), noOfResults));

			System.out.println("Searching " + searchTerm + " on Google at url: " + url);
			Document doc = fetchPage(url);
			Elements headers = doc.select("h2");

			for (Element header : headers) {
				if (!searchType.contains(header.text()))
					continue;
				results.addAll(Arrays.asList(header.nextElementSibling().children().toArray(new Element[] {})));
			}
		} catch (MalformedURLException e) {
			System.out.println("Not able to create google search url with queries. Aborting!\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Cannot fetch search results right now. Aborting!\n" + e.getMessage());
		}
		return results;
	}

}
