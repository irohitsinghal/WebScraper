package personal.scraper.web_crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(Application.class, args);
    }

    /**@Bean public CommandLineRunner commandLineRunner(ScraperService scraperService) {
    return args -> {
    Scanner in = new Scanner(System.in);
    log.info("Enter search Term: ");
    String searchTerm = in.nextLine();
    in.close();

    List list = scraperService.getTopNUsedJavascriptLibraries(searchTerm, 5);
    log.info(String.valueOf(list));
    };
    }*/
}
