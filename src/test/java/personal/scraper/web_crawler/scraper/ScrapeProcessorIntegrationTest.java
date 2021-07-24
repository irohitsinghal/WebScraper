package personal.scraper.web_crawler.scraper;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ScrapeProcessorIntegrationTest {
    private Scraper scraper = new GoogleScraper();
    private ScrapeProcessor app = new ScrapeProcessor(scraper);

    @Test
    public void testProcessReq() {

        Map libraries = app.processRequest("enrique iglesias", 100);
        assertNotEquals(libraries.size(), 0);
    }

    @Test
    @Ignore
    public void testFetchPageException() throws IOException {
        ScrapeProcessor app2 = Mockito.spy(ScrapeProcessor.class);
//		Mockito.doThrow(IOException.class).when(app2).fetchPage(Mockito.anyObject());
        Map libraries = app2.processRequest("Hello World", 100);
        assertEquals(libraries.size(), 0);
    }
}
