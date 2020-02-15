package personal.scraper.web_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;

import personal.scraper.web_crawler.scraper.DefaultScraper;
import personal.scraper.web_crawler.scraper.Scraper;

/**
 * identify top used Javascript libraries
 */
public class App {

	private Scraper scraper;
	private Map<String, AtomicInteger> libraries = new ConcurrentHashMap<String, AtomicInteger>();

	public App(Scraper scraper) {
		this.scraper = scraper;
	}

	void extractJS(Element head) {
		Element scripts[] = head.select("script[src]").toArray(new Element[] {});

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

	void processRequest(String searchTerm) {
		List<Element> results = scraper.scrapeGoogle(searchTerm);

		results.parallelStream().forEach(result -> {
			String childUrl = null;
			try {
				childUrl = result.select("a[href]").first().absUrl("href");
				extractJS(scraper.fetchPage(new URL(childUrl)).head());
			} catch (IOException e) {
				System.out.println("Could not fetch page: " + childUrl);
			}
		});
	}

	int librarySize() {
		return libraries.size();
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter search Term: ");
		String searchTerm = in.nextLine();
		in.close();

		App app = new App(new DefaultScraper(100));
		app.processRequest(searchTerm);

		System.out.println("\nTop 5 most used Javascript libraries:");
		app.libraries.entrySet().stream().sorted(Comparator.comparingInt(a -> a.getValue().get()))
				.map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.toList()).subList(0, 5).stream()
				.forEach(System.out::println);
	}
}
