package personal.scraper.web_crawler.service;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import personal.scraper.web_crawler.scraper.Scraper;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class ScraperServiceImplTest {
    @Autowired
    private Scraper scraper;

    @Autowired
    private ScraperServiceImpl service;

    @Test
    @Ignore
    public void testProcessReq() {
        List<String> list = service.getTopNUsedJavascriptLibraries("enrique iglesias", 100);
        assertNotEquals(list.size(), 0);
    }

    @Test
    @Ignore
    public void testFetchPageException() throws IOException {
//		Mockito.doThrow(IOException.class).when(app2).fetchPage(Mockito.anyObject());
        List<String> list = service.getTopNUsedJavascriptLibraries("Hello World", 100);
        assertEquals(list.size(), 0);
    }
}
