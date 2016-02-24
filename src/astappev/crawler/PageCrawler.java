package astappev.crawler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import astappev.models.Image;
import astappev.views.SearchView;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class PageCrawler extends WebCrawler {

	static final Logger logger = LoggerFactory.getLogger(PageCrawler.class);

	private final static Pattern FILTERS = Pattern.compile(
			".*(\\.(css|js|gif|jpg|png|bmp|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|mp3|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static String crawlDomain;

	private CrawlImages crawledImg;

	private static SearchView searchView;

	private static String outputDir;

	public PageCrawler() {
		crawledImg = new CrawlImages(outputDir);
	}

	public static void configure(SearchView view, String domain, String dir) {
		searchView = view;
		crawlDomain = domain;
		outputDir = dir;
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean ss = !FILTERS.matcher(href).matches() && href.startsWith(crawlDomain);
		return ss;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		crawledImg.incProcessedPages();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			Document doc = Jsoup.parse(html, url);
			Elements media = doc.select("img");
			for (Element img : media) {
				String src = img.attr("abs:src");
				String alt = img.attr("alt").trim();
				String title = img.attr("title").trim();

				if (!src.isEmpty() && (!alt.isEmpty() || !title.isEmpty())) {
					crawledImg.incProcessedImages();
					crawledImg.addCollectedImages(src, url, alt, title);
				}
			}
		}

		// We dump this crawler images after processing every 3 pages
		if ((crawledImg.getProcessedPages() % 10) == 0) {
			updateData();
		}
	}

	/**
	 * This function is called by controller to get the local data of this
	 * crawler when job is finished
	 */
	@Override
	public Object getMyLocalData() {
		return crawledImg;
	}

	/**
	 * This function is called by controller before finishing the job. You can
	 * put whatever stuff you need here.
	 */
	@Override
	public void onBeforeExit() {
		updateData();
	}

	public void updateData() {
		int id = getMyId();

		for (Entry<String, Image> img : crawledImg.getFilteredImages().entrySet()) {
			logger.debug(img.getKey() + " " + img.getValue());
		}

		if (searchView != null) {
			searchView.addProcessedPages(crawledImg.getProcessedPages());
			crawledImg.setProcessedPages(0);
			searchView.addProcessedImages(crawledImg.getProcessedImages());
			crawledImg.setProcessedImages(0);
			searchView.addTotalFaces(crawledImg.getTotalFaces());
			crawledImg.setTotalFaces(0);
			searchView.addTotalNames(crawledImg.getTotalNames());
			crawledImg.setTotalNames(0);
			
			for (Entry<String, Image> entry : crawledImg.getFilteredImages().entrySet()) {
				searchView.getResults().add(entry.getValue());
			}
			crawledImg.getFilteredImages().clear();
		}

		logger.info("Crawler " + id + " > Processed Pages: " + crawledImg.getProcessedPages());
		logger.info("Crawler " + id + " > Total Images: " + crawledImg.getProcessedImages());
	}
}
