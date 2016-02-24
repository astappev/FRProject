package astappev.service;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import astappev.crawler.CrawlImages;
import astappev.crawler.PageCrawler;
import astappev.views.SearchView;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SiteProcessor implements Serializable {

	private static final long serialVersionUID = -3720031984397668636L;

	static final Logger logger = LoggerFactory.getLogger(SiteProcessor.class);

	private static String rootFolder = "E:\\fr-project\\crawl";

	private String storageFolder = "E:\\fr-project\\images\\target\\";

	private int numberOfCrawlers = 2;

	private CrawlController controller;

	public SiteProcessor() throws Exception {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(rootFolder);
		// config.setMaxPagesToFetch(10);
		// config.setPolitenessDelay(1000);
		// config.setIncludeBinaryContentInCrawling(true);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		controller = new CrawlController(config, pageFetcher, robotstxtServer);
	}

	public void setNumberOfCrawlers(int num) {
		this.numberOfCrawlers = num;
	}

	public void run(String url, SearchView searchView) throws Exception {
		controller.addSeed(url);
		PageCrawler.configure(searchView, getDomainName(url), storageFolder);

		controller.start(PageCrawler.class, numberOfCrawlers);

		List<Object> crawlersLocalData = controller.getCrawlersLocalData();
		long totalImages = 0;
		int totalProcessedPages = 0;
		for (Object localData : crawlersLocalData) {
			CrawlImages stat = (CrawlImages) localData;
			totalImages += stat.getCollectedImages().size();
			totalProcessedPages += stat.getProcessedPages();
		}

		logger.info("Aggregated Statistics:");
		logger.info("Processed Pages: " + totalProcessedPages);
		logger.info("Total Images collected: " + totalImages);
	}

	public static String getDomainName(String url) throws URISyntaxException {
		int slashslash = url.indexOf("//") + 2;
		return url.substring(0, url.indexOf('/', slashslash));
	}

	public static void main(String[] args) throws Exception {
		SiteProcessor sp = new SiteProcessor();
		sp.run("https://en.wikipedia.org/wiki/American_football", null);
	}
}
