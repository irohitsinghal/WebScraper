package personal.scraper.web_crawler.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class WebUtils {
    public static final String CHARSET = "UTF-8";

    public static Document fetchPage(URL url) throws IOException {
        return Jsoup.connect(url.toString()).userAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
                .header("Content-Language", "en-US").get();
    }
}
