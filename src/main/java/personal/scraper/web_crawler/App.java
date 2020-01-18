package personal.scraper.web_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

	private static Map<String, Integer> libraries = new ConcurrentHashMap<String, Integer>();

	static Document fetchPage(URL url) throws IOException {
		return Jsoup.connect(url.toString()).userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
				.header("Content-Language", "en-US").get();
	}

	static void extractJS(Element head) {
		Elements scripts = head.select("script[src]");

		for (Element script : scripts) {
			try {
				URL url = new URL(script.absUrl("src"));
				String tempPaths[] = url.getPath().split("/");

				String key = tempPaths[tempPaths.length - 1];
				libraries.put(key, libraries.getOrDefault(key, 0) + 1);
			} catch (MalformedURLException e) {
				System.out.println("Could not get js file: " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		System.out.print("Enter search Term: ");
		String searchTerm = in.nextLine();
		in.close();

		String google = "https://www.google.com/search?q=%s&num=%s";
		List<String> searchType = Arrays.asList("Web results", "Twitter results");
		String CHARSET = "UTF-8";

		try {
			URL url = new URL(String.format(google, URLEncoder.encode(searchTerm, CHARSET)));

			System.out.println("Searching " + searchTerm + " on Google at url: " + url);
			Document doc = fetchPage(url);
			Elements headers = doc.select("h2");

			for (Element header : headers) {
				if (!searchType.contains(header.text()))
					continue;

				Elements results = header.nextElementSibling().children();

				for (Element result : results) {
					String childUrl = null;
					childUrl = result.select("a[href]").first().absUrl("href");
					extractJS(fetchPage(new URL(childUrl)).head());
				}

			}

		} catch (MalformedURLException e) {
			System.out.println("Not able to create google search url with queries. Aborting!\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Cannot fetch search results right now. Aborting!\n" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
