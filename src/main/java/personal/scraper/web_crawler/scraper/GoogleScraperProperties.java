package personal.scraper.web_crawler.scraper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "scraper.google", ignoreUnknownFields = false)
@Data
public class GoogleScraperProperties {
    private boolean enabled;
    private String searchUrl;
    private List<String> searchType;
}
