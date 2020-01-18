package personal.scraper.web_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Web crawler to scrape google results and identify top used Javascript
 * libraries
 */
public class App {

	private Map<String, Integer> libraries = new ConcurrentHashMap<String, Integer>();

	Document fetchPage(URL url) throws IOException {
		return Jsoup.connect(url.toString()).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
				.header("Content-Language", "en-US").get();
	}

	void extractJS(Element head) {
		Element scripts[] = head.select("script[src]").toArray(new Element[] {});

		Stream.of(scripts).parallel().forEach(script -> {
			try {
				URL url = new URL(script.absUrl("src"));
				String tempPaths[] = url.getPath().split("/");

				String key = tempPaths[tempPaths.length - 1];
				libraries.put(key, libraries.getOrDefault(key, 0) + 1);
			} catch (MalformedURLException e) {
				System.out.println("Could not get js file: " + e.getMessage());
			}
		});
	}

	void processRequest(String searchTerm) {
		String google = "https://www.google.com/search?q=%s&num=%s";
		List<String> searchType = Arrays.asList("Web results", "Twitter results");
		int noOfResults = 20;
		String CHARSET = "UTF-8";

		try {
			URL url = new URL(String.format(google, URLEncoder.encode(searchTerm, CHARSET), noOfResults));

			System.out.println("Searching " + searchTerm + " on Google at url: " + url);
			Document doc = fetchPage(url);
			Elements headers = doc.select("h2");

			List<Element> results = new ArrayList<>();
			for (Element header : headers) {
				if (!searchType.contains(header.text()))
					continue;

				results.addAll(Arrays.asList(header.nextElementSibling().children().toArray(new Element[] {})));
			}

			results.parallelStream().forEach(result -> {
				String childUrl = null;
				try {
					childUrl = result.select("a[href]").first().absUrl("href");
					extractJS(fetchPage(new URL(childUrl)).head());
				} catch (IOException e) {
					System.out.println("Could not fetch page: " + childUrl);
				}
			});

			System.out.println("\nTop 5 most used Javascript libraries:");
			libraries.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
					.map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.toList()).subList(0, 5).stream()
					.forEach(System.out::println);

		} catch (MalformedURLException e) {
			System.out.println("Not able to create google search url with queries. Aborting!\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Cannot fetch search results right now. Aborting!\n" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	int librarySize() {
		return libraries.size();
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter search Term: ");
		String searchTerm = in.nextLine();
		in.close();

		App app = new App();
		app.processRequest(searchTerm);
	}
}
