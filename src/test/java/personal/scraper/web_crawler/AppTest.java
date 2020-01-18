package personal.scraper.web_crawler;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class AppTest {
	private App app = new App();

	@Test
	public void testProcessReq() {
		app.processRequest("enrique iglesias");
		assertNotEquals(app.librarySize(), 0);
	}
}
