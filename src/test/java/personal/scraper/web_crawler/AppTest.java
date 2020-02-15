package personal.scraper.web_crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import personal.scraper.web_crawler.scraper.DefaultScraper;
import personal.scraper.web_crawler.scraper.Scraper;

public class AppTest {
	private Scraper scraper = new DefaultScraper(100);
	private App app = new App(scraper);

	@Test
	public void testProcessReq() {

		app.processRequest("enrique iglesias");
		assertNotEquals(app.librarySize(), 0);
	}

	@Test
	@Ignore
	public void testFetchPageException() throws IOException {
		App app2 = Mockito.spy(App.class);
//		Mockito.doThrow(IOException.class).when(app2).fetchPage(Mockito.anyObject());
		app2.processRequest("Hello World");
		assertEquals(app2.librarySize(), 0);
	}

}
